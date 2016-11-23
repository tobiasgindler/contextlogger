package io.tracee.contextlogger.contextprovider.core.tracee;

import io.tracee.contextlogger.contextprovider.api.Flatten;
import io.tracee.contextlogger.contextprovider.api.ImplicitContext;
import io.tracee.contextlogger.contextprovider.api.ImplicitContextData;
import io.tracee.contextlogger.contextprovider.api.Order;
import io.tracee.contextlogger.contextprovider.api.ProfileConfig;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProvider;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;
import io.tracee.contextlogger.contextprovider.core.utility.NameValuePair;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Common context data provider.
 */
@SuppressWarnings("unused")
@TraceeContextProvider(displayName = "tracee", order = Order.TRACEE)
@ProfileConfig(basic = true, enhanced = true, full = true)
public final class TraceeMdcContextProvider implements ImplicitContextData {

	public TraceeMdcContextProvider() {

	}

	@Override
	public ImplicitContext getImplicitContext() {
		return CoreImplicitContextProviders.TRACEE;
	}

	@SuppressWarnings("unused")
	@Flatten
	@TraceeContextProviderMethod(displayName = "DYNAMIC", order = 10)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public List<NameValuePair<String>> getNameValuePairs() {

		final List<NameValuePair<String>> list = new ArrayList<NameValuePair<String>>();

		final Map<String, String> keys = MDC.getCopyOfContextMap();
		if (keys != null) {
			for (Map.Entry<String, String> entry : keys.entrySet()) {
				list.add(new NameValuePair<String>(entry.getKey(), entry.getValue()));
			}
		}
		return !list.isEmpty() ? list : null;
	}
}
