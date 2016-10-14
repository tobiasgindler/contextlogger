package io.tracee.contextlogger.agent.metrics;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class MeasuringMetricsInterceptor {

	@RuntimeType
	public static Object intercept(@Origin Class calledClass, @Origin Method calledMethod, @SuperCall Callable<?> interceptedCall, @AllArguments Object... args) throws Exception {

		long startTime = System.currentTimeMillis();

		try {

			return interceptedCall.call();

		} finally {
			MeasuredMetricReporter.reportTime(calledClass.getCanonicalName() + "." + calledMethod.getName(), System.currentTimeMillis() - startTime);
		}

	}

}
