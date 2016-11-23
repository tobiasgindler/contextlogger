package io.tracee.contextlogger.api;

import io.tracee.contextlogger.connector.LogLevel;

/**
 * The context logger interface used to enable fluent API.
 */
public interface ContextLogger extends ToStringBuilder {

	/**
	 * Creates a string representation of the passed instancesToLog and passes them to all configured connectors.
	 *
	 * @param instancesToLog The instances to be converted into a string.
	 */
	void log(Object... instancesToLog);

	/**
	 * Creates a string representation of the passed instancesToLog and passes them to all configured connectors.
	 * Adds a prefixed message string for {@link io.tracee.contextlogger.connector.LogConnector}.
	 *
	 * @param logLevel        The messages log level
	 * @param prefixedMessage The message to be prefixed with the LogConnector
	 * @param instancesToLog  The instances to be converted into a string.
	 */
	void logWithPrefixedMessage(LogLevel logLevel, String prefixedMessage, Object... instancesToLog);

	/**
	 * Creates a string representation of the passed instancesToLog and passes them to all configured connectors.
	 * Adds a prefixed message string for {@link io.tracee.contextlogger.connector.LogConnector}.
	 *
	 * @param prefixedMessage The message to be prefixed with the LogConnector
	 * @param instancesToLog  The instances to be converted into a string.
	 */
	void logWithPrefixedMessage(String prefixedMessage, Object... instancesToLog);


}
