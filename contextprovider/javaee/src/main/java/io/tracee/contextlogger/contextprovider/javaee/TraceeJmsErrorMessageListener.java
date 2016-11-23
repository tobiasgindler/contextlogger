package io.tracee.contextlogger.contextprovider.javaee;

import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.connector.LogLevel;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import java.lang.reflect.Method;

/**
 * Message listener to detect exceptions that happened during javaee message processing.
 * In case of an exception contextual information will be written to the log.
 */
public class TraceeJmsErrorMessageListener {

	static final String JSON_PREFIXED_MESSAGE = "TRACEE JMS ERROR CONTEXT LOGGING LISTENER  : ";

	@SuppressWarnings("unused")
	public TraceeJmsErrorMessageListener() {

	}

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		try {
			return ctx.proceed();
		} catch (Exception e) {
			final boolean isMdbInvocation = isMessageListenerOnMessageMethod(ctx.getMethod());

			if (isMdbInvocation) {
				TraceeContextLogger.create().enforceOrder().apply()
						.logWithPrefixedMessage(LogLevel.ERROR, JSON_PREFIXED_MESSAGE, CoreImplicitContextProviders.COMMON, CoreImplicitContextProviders.TRACEE, ctx, e);
			}

			throw e;
		}
	}

	boolean isMessageListenerOnMessageMethod(Method method) {
		return "onMessage".equals(method.getName()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Message.class;
	}

}
