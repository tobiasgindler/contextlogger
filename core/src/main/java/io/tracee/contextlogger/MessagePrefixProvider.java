package io.tracee.contextlogger;

import io.tracee.contextlogger.connector.LogLevel;

/**
 * Provides a strictly formatted log message prefix.
 */
public final class MessagePrefixProvider {

	public static final LogLevel DEFAULT_LEVEL = LogLevel.INFO;

	static final String JSON_PREFIXED_MESSAGE = "TRACEE_CL_{1}[{2}]  : ";

	public static String provideLogMessagePrefix(final LogLevel logLevel, final String type) {
		String prefix = JSON_PREFIXED_MESSAGE.replace("{1}", logLevel != null ? logLevel.name() : DEFAULT_LEVEL.name());
		prefix = prefix.replace("{2}", type != null ? type : "");
		return prefix;
	}

	public static String provideLogMessagePrefix(final LogLevel logLevel, final Class type) {
		return provideLogMessagePrefix(logLevel, type != null ? type.getSimpleName() : null);
	}
}
