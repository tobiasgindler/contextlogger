package io.tracee.contextlogger.contextprovider.agent;

import io.tracee.contextlogger.contextprovider.api.Order;
import io.tracee.contextlogger.contextprovider.api.ProfileConfig;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProvider;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.api.WrappedContextData;

import java.util.Arrays;
import java.util.List;

@TraceeContextProvider(displayName = "agent-interceptor", order = Order.AGENT)
@ProfileConfig(basic = true, enhanced = true, full = true)
public class AgentContextProvider implements WrappedContextData<AgentDataWrapper> {


	private AgentDataWrapper agentDataWrapper;

	@SuppressWarnings("unused")
	public AgentContextProvider() {
	}

	@SuppressWarnings("unused")
	public AgentContextProvider(final AgentDataWrapper agentDataWrapper) {
		this.agentDataWrapper = agentDataWrapper;
	}

	@Override
	public final void setContextData(Object instance) throws ClassCastException {
		this.agentDataWrapper = (AgentDataWrapper) instance;
	}

	@Override
	public AgentDataWrapper getContextData() {
		return this.agentDataWrapper;
	}

	public final Class<AgentDataWrapper> getWrappedType() {
		return AgentDataWrapper.class;
	}


	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "class", order = 20)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getClazz() {
		if (agentDataWrapper != null && agentDataWrapper.getClazz() != null) {
			return agentDataWrapper.getClazz().getCanonicalName();
		}
		return null;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "method", order = 30)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getMethod() {
		if (agentDataWrapper != null && agentDataWrapper.getMethod() != null) {
			return agentDataWrapper.getMethod().toGenericString();
		}
		return null;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "parameters", order = 40)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final List<Object> getParameters() {

		if (agentDataWrapper != null && agentDataWrapper.getParameters() != null) {
			// output parameters
			return Arrays.asList(agentDataWrapper.getParameters());
		}
		return null;
	}


}
