package io.tracee.contextlogger.contextprovider.springmvc.contextprovider;

import io.tracee.contextlogger.contextprovider.api.ProfileConfig;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProvider;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.api.WrappedContextData;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.List;

/*
 * Context provider for {@link HandlerMethod}.
 */

@TraceeContextProvider(displayName = "handlerMethod")
@ProfileConfig(basic = true, enhanced = true, full = true)
public class HandlerMethodContextProvider implements WrappedContextData<HandlerMethod> {

	private HandlerMethod handlerMethod;

	@SuppressWarnings("unused")
	public HandlerMethodContextProvider() {
	}

	@SuppressWarnings("unused")
	public HandlerMethodContextProvider(final HandlerMethod handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	@Override
	public final void setContextData(Object instance) throws ClassCastException {
		this.handlerMethod = (HandlerMethod) instance;
	}

	@Override
	public HandlerMethod getContextData() {
		return this.handlerMethod;
	}

	public final Class<HandlerMethod> getWrappedType() {
		return HandlerMethod.class;
	}

	public static HandlerMethodContextProvider wrap(final HandlerMethod handlerMethod) {
		return new HandlerMethodContextProvider(handlerMethod);
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "type", order = 20)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getType() {
		if (handlerMethod != null && handlerMethod.getBeanType() != null) {
			return handlerMethod.getBeanType().getCanonicalName();
		}
		return null;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "method", order = 30)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getMethod() {
		if (handlerMethod != null && handlerMethod.getMethod() != null) {
			return handlerMethod.getMethod().getName();
		}
		return null;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "parameters", order = 40)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final List<MethodParameter> getParameters() {

		if (handlerMethod != null && handlerMethod.getMethodParameters() != null) {

			// output parameters
			return Arrays.asList(handlerMethod.getMethodParameters());
		}

		return null;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "serialized-target-instance", order = 50)
	@ProfileConfig(basic = false, enhanced = true, full = true)
	public final Object getSerializedTargetInstance() {
		if (handlerMethod != null) {
			// output invoked instance
			return handlerMethod.getBean();

		}

		return null;
	}

}
