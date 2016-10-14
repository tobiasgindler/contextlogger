package io.tracee.contextlogger.contextprovider.agent;

import io.tracee.contextlogger.contextprovider.api.TraceeContextProviderServiceProvider;

/**
 * Service provider for all core context providers.
 */
public class AgentContextProviderServiceProvider implements TraceeContextProviderServiceProvider {

	public static final Class[] IMPLICIT_CONTEXT_PROVIDER = {};
	public static final Class[] CONTEXT_PROVIDER = {AgentContextProvider.class};

	@Override
	public Class[] getImplicitContextProvider() {
		return IMPLICIT_CONTEXT_PROVIDER;
	}

	@Override
	public Class[] getContextProvider() {
		return CONTEXT_PROVIDER;
	}


}
