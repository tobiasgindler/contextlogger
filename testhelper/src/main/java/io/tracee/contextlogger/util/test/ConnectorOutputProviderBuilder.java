package io.tracee.contextlogger.util.test;

import io.tracee.contextlogger.connector.ConnectorOutputProvider;
import io.tracee.contextlogger.connector.LogConnectorOutputProvider;
import io.tracee.contextlogger.connector.LogLevel;

/**
 * Creates a {@link io.tracee.contextlogger.connector.ConnectorOutputProvider} or {@link io.tracee.contextlogger.connector.LogConnectorOutputProvider} instance.
 */
public class ConnectorOutputProviderBuilder {

	public static ConnectorOutputProvider createConnectorOutputProvider(final String prefix, final String json) {

		if (prefix == null) {
			return new ConnectorOutputProvider() {

				public ConnectorOutputProvider excludeContextProviders(final Class... contextProvidersToInclude) {
					return this;
				}

				public String provideOutput() {
					return json;
				}

				public LogLevel getLogLevel() {
					return LogLevel.INFO;
				}

			};
		} else {
			return new LogConnectorOutputProvider() {

				public ConnectorOutputProvider excludeContextProviders(final Class... contextProvidersToInclude) {
					return this;
				}

				public String provideOutput() {
					return json;
				}

				public String getPrefix() {
					return prefix;
				}

				public LogLevel getLogLevel() {
					return LogLevel.INFO;
				}

			};
		}

	}

}
