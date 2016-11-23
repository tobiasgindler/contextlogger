package io.tracee.contextlogger.agent;

import io.tracee.contextlogger.MessagePrefixProvider;
import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.connector.LogLevel;
import io.tracee.contextlogger.contextprovider.agent.AgentDataWrapper;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;


public class ExceptionContextloggerAdvice {

	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void stopMeasuring(@Advice.Origin Class calledClass, @Advice.Origin Method calledMethod, @Advice.BoxedArguments Object[] args, @Advice.Thrown Throwable exception) {
		if (exception != null) {

			TraceeContextLogger
					.create()
					.enforceOrder()
					.apply()
					.logWithPrefixedMessage(LogLevel.ERROR, MessagePrefixProvider.provideLogMessagePrefix(LogLevel.ERROR, ContextloggerInterceptor.class), CoreImplicitContextProviders.COMMON,
							CoreImplicitContextProviders.TRACEE, AgentDataWrapper.wrap(calledClass, calledMethod, args), exception);

		}

	}
}
