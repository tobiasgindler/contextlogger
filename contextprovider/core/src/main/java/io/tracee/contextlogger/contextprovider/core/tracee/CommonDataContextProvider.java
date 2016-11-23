package io.tracee.contextlogger.contextprovider.core.tracee;

import io.tracee.contextlogger.contextprovider.api.ImplicitContext;
import io.tracee.contextlogger.contextprovider.api.ImplicitContextData;
import io.tracee.contextlogger.contextprovider.api.Order;
import io.tracee.contextlogger.contextprovider.api.ProfileConfig;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProvider;
import io.tracee.contextlogger.contextprovider.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.core.CoreImplicitContextProviders;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

/**
 * Common context data provider.
 */
@TraceeContextProvider(displayName = "common", order = Order.COMMON)
@ProfileConfig(basic = true, enhanced = true, full = true)
public class CommonDataContextProvider implements ImplicitContextData {

	public static final String SYSTEM_PROPERTY_PREFIX = "io.tracee.contextlogger.";
	public static final String SYSTEM_PROPERTY_NAME_STAGE = SYSTEM_PROPERTY_PREFIX + "tracee-standard-stage";
	public static final String SYSTEM_PROPERTY_NAME_SYSTEM = SYSTEM_PROPERTY_PREFIX + "tracee-standard-system";

	@Override
	public final ImplicitContext getImplicitContext() {
		return CoreImplicitContextProviders.COMMON;
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "timestamp", order = 10)
	@ProfileConfig(basic = true, enhanced = true, full = true)

	public final Date getTimestamp() {
		return Calendar.getInstance().getTime();
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "stage", order = 20)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getStage() {
		return getSystemProperty(SYSTEM_PROPERTY_NAME_STAGE);
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "system-name", order = 30)
	@ProfileConfig(basic = true, enhanced = true, full = true)

	public final String getSystemName() {

		String systemName = getSystemProperty(SYSTEM_PROPERTY_NAME_SYSTEM);

		if (systemName == null) {
			try {
				systemName = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				// ignore
			}
		}

		return systemName;

	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "thread-name", order = 40)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final String getThreadName() {
		return Thread.currentThread().getName();
	}

	@SuppressWarnings("unused")
	@TraceeContextProviderMethod(displayName = "thread-id", order = 50)
	@ProfileConfig(basic = true, enhanced = true, full = true)
	public final Long getThreadId() {
		return Thread.currentThread().getId();
	}

	public String getSystemProperty(final String attributeName) {
		return System.getProperty(attributeName);
	}
}
