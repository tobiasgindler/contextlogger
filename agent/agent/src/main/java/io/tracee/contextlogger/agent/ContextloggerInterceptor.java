package io.tracee.contextlogger.agent;


import io.tracee.contextlogger.MessagePrefixProvider;
import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.api.internal.MessageLogLevel;
import io.tracee.contextlogger.contextprovider.agent.AgentDataWrapper;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;


@Slf4j
public class ContextloggerInterceptor {

	@RuntimeType
	public static Object intercept(@Origin Class calledClass, @Origin Method calledMethod, @SuperCall Callable<?> interceptedCall, @AllArguments Object... args) throws Exception {

		try {


			TraceeContextLogger
					.create()
					.enforceOrder()
					.apply()
					.logWithPrefixedMessage(MessagePrefixProvider.provideLogMessagePrefix(MessageLogLevel.INFO, ContextloggerInterceptor.class), CoreImplicitContextProviders.COMMON,
							CoreImplicitContextProviders.TRACEE, AgentDataWrapper.wrap(calledClass, calledMethod, args));

			return interceptedCall.call();
		} catch (Exception e) {

			// rethrow exception
			throw e;

		}

	}
}
