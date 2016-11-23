package io.tracee.contextlogger.contextprovider.springmvc;

import io.tracee.contextlogger.MessagePrefixProvider;
import io.tracee.contextlogger.TraceeContextLogger;
import io.tracee.contextlogger.connector.LogLevel;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceeContextLoggerHandlerInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) throws Exception {
		return true;
	}

	@Override
	public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o,
						   final ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o,
								final Exception e) throws Exception {

		// Output context data in case of an exception
		if (e != null) {

			TraceeContextLogger
					.create()
					.enforceOrder()
					.apply()
					.logWithPrefixedMessage(LogLevel.ERROR, MessagePrefixProvider.provideLogMessagePrefix(LogLevel.ERROR, TraceeContextLoggerHandlerInterceptor.class),
							CoreImplicitContextProviders.COMMON, CoreImplicitContextProviders.TRACEE, o, httpServletRequest, httpServletResponse,
							httpServletRequest.getSession(false), e);

		}

	}
}
