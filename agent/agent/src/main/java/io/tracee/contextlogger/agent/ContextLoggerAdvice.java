package io.tracee.contextlogger.agent;


import io.tracee.contextlogger.MessagePrefixProvider;
import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.connector.LogLevel;
import io.tracee.contextlogger.contextprovider.agent.AgentDataWrapper;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;

public class ContextLoggerAdvice {

	@Advice.OnMethodEnter
	public static void outputCall(@Advice.Origin Class calledClass, @Advice.Origin Method calledMethod, @Advice.BoxedArguments Object[] args) {

		TraceeContextLogger
				.create()
				.enforceOrder()
				.apply()
				.logWithPrefixedMessage(LogLevel.INFO, MessagePrefixProvider.provideLogMessagePrefix(LogLevel.INFO, ContextloggerInterceptor.class), CoreImplicitContextProviders.COMMON,
						CoreImplicitContextProviders.TRACEE, AgentDataWrapper.wrap(calledClass, calledMethod, args));

	}


}
