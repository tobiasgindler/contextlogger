package io.tracee.contextlogger.agent;


import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.agent.api.Measured;
import io.tracee.contextlogger.agent.configuration.InstrumentationConfig;
import io.tracee.contextlogger.agent.configuration.InstrumentationConfigLoader;
import io.tracee.contextlogger.agent.listeners.TransformationErrorLoggingListener;
import io.tracee.contextlogger.agent.metrics.MeasuredMetricReporter;
import io.tracee.contextlogger.agent.metrics.MeasuringMetricsAdvice;
import io.tracee.contextlogger.agent.metrics.MeasuringMetricsInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.isSynthetic;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;
import static net.bytebuddy.matcher.ElementMatchers.not;

@Slf4j
public class TraceeContextloggerAgent {

	public static enum InstrumentationMethod {
		ADVICE, INTERCEPTOR;
	}

	private final static InstrumentationMethod INSTRUMENTATION_METHOD = InstrumentationMethod.ADVICE;


	@AllArgsConstructor
	public static class ContextLoggerTransformer implements AgentBuilder.Transformer {


		private final InstrumentationConfig[] instrumentedConfigurations;
		private Class interceptor;
		private Class advice;


		@Override
		public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {
			log.info("Apply to type : " + typeDescription.getCanonicalName());


			// check if it should applied generally and or collect regex strings for type matches
			boolean shouldBeAppliedForAllCases = false;
			List<String> methodRegexes = new ArrayList<String>();
			for (InstrumentationConfig config : instrumentedConfigurations) {

				if (config.classMatches(typeDescription.getCanonicalName())) {
					if (config.getMethodNameRegex() == null) {
						shouldBeAppliedForAllCases = true;
						break;
					} else {
						methodRegexes.add(config.getMethodNameRegex());
					}
				}

			}

			ElementMatcher.Junction matcher = null;

			if (shouldBeAppliedForAllCases) {
				matcher = ElementMatchers.any();
			} else if (methodRegexes.isEmpty()) {
				matcher = ElementMatchers.none();
			} else {
				for (String regex : methodRegexes) {
					if (matcher == null) {
						matcher = ElementMatchers.nameMatches(regex);
					} else {
						matcher = matcher.or(ElementMatchers.nameMatches(regex));
					}
				}
			}

			if (INSTRUMENTATION_METHOD == InstrumentationMethod.INTERCEPTOR) {
				return builder.method(matcher).intercept(MethodDelegation.to(interceptor));
			} else {
				return builder.visit(Advice.to(advice).on((ElementMatchers.<MethodDescription.InDefinedShape>isMethod()).and(matcher)));
			}


		}
	}

	public static class MeasuredMetricsTransformer implements AgentBuilder.Transformer {


		@Override
		public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader) {


			ElementMatcher.Junction matcher = ElementMatchers.isAnnotatedWith(Measured.class);

			InstrumentationConfig[] instrumentationConfigs = InstrumentationConfigLoader.getInstance().getMetricsConfig();
			if (instrumentationConfigs != null && instrumentationConfigs.length > 0) {
				for (InstrumentationConfig instrumentationConfig : instrumentationConfigs) {
					if (instrumentationConfig.classMatches(typeDescription.getCanonicalName())) {
						matcher = matcher.or(ElementMatchers.nameMatches(instrumentationConfig.getMethodNameRegex()));
					}
				}
			}

			if (INSTRUMENTATION_METHOD == InstrumentationMethod.INTERCEPTOR) {
				return builder.method(matcher).intercept(MethodDelegation.to(MeasuringMetricsInterceptor.class));
			} else {
				return builder.visit(Advice.to(MeasuringMetricsAdvice.class).on((ElementMatchers.<MethodDescription.InDefinedShape>isMethod()).and(matcher)));
			}
		}
	}


	public static void premain(String agentArguments, Instrumentation instrumentation) {

		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		log.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());

		MeasuredMetricReporter.startJmxReporter();

		log.info("Starting TraceeContextloggerAgent");
		TraceeContextLogger.create();

		log.info("Configure ignored packages");
		ElementMatcher.Junction ignoredPackagesJunction = nameStartsWith("java")
				.or(nameStartsWith("com.sun."))
				.or(nameStartsWith("sun."))
				.or(nameStartsWith("jdk."))
				.or(nameStartsWith("net.bytebuddy."))
				.or(nameStartsWith("org.slf4j.").and(not(nameStartsWith("org.slf4j.impl."))))
				.or(nameStartsWith("io.tracee.contextlogger."))
				//.or(nameContains("auxiliary"))
				.or(nameStartsWith("ch.qos.logback."))
				.or(nameStartsWith("com.codahale.metrics.")).or(isSynthetic());

		if (InstrumentationConfigLoader.getInstance().getDeactivatedPackages() != null) {
			for (String ignoredPackage : InstrumentationConfigLoader.getInstance().getDeactivatedPackages()) {
				ignoredPackagesJunction = ignoredPackagesJunction.or(nameStartsWith(ignoredPackage));
			}
		}


		AgentBuilder agentBuilder = new AgentBuilder.Default(
				new ByteBuddy().with(TypeValidation.ENABLED).with(MethodGraph.Empty.INSTANCE)
		)
				//.disableClassFormatChanges()
				.with(new TransformationErrorLoggingListener())
				//.with(AgentBuilder.RedefinitionStrategy.)
				//.with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
				//.with(AgentBuilder.TypeStrategy.Default.REBASE)
				.ignore(ignoredPackagesJunction)
				.disableClassFormatChanges();


		agentBuilder.type(ElementMatchers.<TypeDescription>any()).transform(new ContextLoggerTransformer(InstrumentationConfigLoader.getInstance().getContextLoggerConfig(), ContextloggerInterceptor.class, ContextLoggerAdvice.class)).installOn(instrumentation);
		agentBuilder.type(ElementMatchers.<TypeDescription>any()).transform(new ContextLoggerTransformer(InstrumentationConfigLoader.getInstance().getExceptionContextLoggerConfig(), ExceptionContextloggerInterceptor.class, ExceptionContextloggerAdvice.class)).installOn(instrumentation);


		// Enable measuring of metrics
		agentBuilder.type(ElementMatchers.<TypeDescription>any()).transform(new MeasuredMetricsTransformer()).installOn(instrumentation);


	}


}
