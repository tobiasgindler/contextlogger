package io.tracee.contextlogger.connector;

/**
 * Provides prefix message and log level to use with LogConnector.
 */
public interface LogConnectorOutputProvider extends ConnectorOutputProvider {

	/**
	 * Gets the prefix used to provide a prefix message for log output.
	 *
	 * @return
	 */
	String getPrefix();

	/**
	 * Gets the log level to use for log output.
	 *
	 * @return
	 */
	LogLevel getLogLevel();
}
