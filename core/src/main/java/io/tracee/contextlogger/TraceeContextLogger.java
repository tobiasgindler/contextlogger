package io.tracee.contextlogger;

import io.tracee.contextlogger.api.ConfigBuilder;
import io.tracee.contextlogger.api.ContextLogger;
import io.tracee.contextlogger.connector.ConnectorOutputProvider;
import io.tracee.contextlogger.connector.LogConnectorOutputProvider;
import io.tracee.contextlogger.connector.LogLevel;
import io.tracee.contextlogger.impl.ConfigBuilderImpl;
import io.tracee.contextlogger.impl.ContextLoggerConfiguration;
import io.tracee.contextlogger.outputgenerator.api.TraceeContextStringRepresentationBuilder;

/**
 * The main context logger class.
 * This class is used to generate context information.
 */

public final class TraceeContextLogger extends AbstractToStringBuilder<ContextLogger> implements LogConnectorOutputProvider {

	private ConnectorFactory connectorsWrapper;

	private LogLevel logLevel;

	private String prefix;

	private TraceeContextLogger(ContextLoggerConfiguration contextLoggerConfiguration) {
		super(contextLoggerConfiguration);
		initConnectors();
	}

	private TraceeContextLogger(TraceeContextLogger instanceToClone) {
		super(instanceToClone);

		this.connectorsWrapper = instanceToClone.connectorsWrapper;

		this.prefix = instanceToClone.prefix;

	}

	/**
	 * Initializes all available connectors.
	 */
	private void initConnectors() {
		connectorsWrapper = new ConnectorFactory();
	}

	public static ConfigBuilder<ContextLogger> create() {

		TraceeContextLogger contextLoggerInstance = new TraceeContextLogger(ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration());
		return new ConfigBuilderImpl<ContextLogger>(contextLoggerInstance);

	}

	public static ContextLogger createDefault() {
		return create().apply();
	}

	@Override
	public void log(Object... instancesToLog) {
		this.logWithPrefixedMessage(null, instancesToLog);
	}

	@Override
	public void logWithPrefixedMessage(String prefixedMessage, Object... instancesToLog) {
		logWithPrefixedMessage(LogLevel.INFO, prefixedMessage, instancesToLog);
	}

	@Override
	public void logWithPrefixedMessage(LogLevel logLevel, String prefixedMessage, Object... instancesToLog) {

		this.logLevel = logLevel;
		this.prefix = prefixedMessage;
		this.setObjectsToProcess(instancesToLog);

		this.connectorsWrapper.sendErrorReportToConnectors(this);
	}


	@Override
	public ConnectorOutputProvider excludeContextProviders(final Class... contextProvidersToInclude) {

		TraceeContextLogger traceeContextLogger = new TraceeContextLogger(this);
		TraceeContextStringRepresentationBuilder traceeContextStringRepresentationBuilder = this.getStringRepresentationBuilder()
				.cloneStringRepresentationBuilder();

		for (Class type : contextProvidersToInclude) {

			if (type != null) {
				traceeContextStringRepresentationBuilder.getManualContextOverrides().put(type.getCanonicalName(), Boolean.FALSE);
			}
		}

		traceeContextLogger.setStringRepresentationBuilder(traceeContextStringRepresentationBuilder);

		return traceeContextLogger;
	}

	@Override
	public String provideOutput() {

		return this.getStringRepresentationBuilder().createStringRepresentation(this.getObjectsToProcess());
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public LogLevel getLogLevel() {
		return logLevel;
	}
}
