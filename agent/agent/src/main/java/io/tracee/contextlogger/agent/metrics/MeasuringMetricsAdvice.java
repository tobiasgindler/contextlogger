package io.tracee.contextlogger.agent.metrics;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.OnMethodEnter;

import java.lang.reflect.Method;


public class MeasuringMetricsAdvice {

	@OnMethodEnter
	public static Long startMeasuring() {
		return System.currentTimeMillis();
	}

	@Advice.OnMethodExit
	public static void stopMeasuring(@Advice.Origin Class calledClass, @Advice.Origin Method calledMethod, @Advice.Enter Long startTimeInMs) {
		MeasuredMetricReporter.reportTime(calledClass.getCanonicalName() + "." + calledMethod.getName(), System.currentTimeMillis() - startTimeInMs);
	}

}
