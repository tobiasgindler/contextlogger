package io.tracee.contextlogger.connector;

import io.tracee.contextlogger.contextprovider.core.tracee.CommonDataContextProvider;
import io.tracee.contextlogger.contextprovider.core.tracee.TraceeMdcContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Connector to send error reports to the logger.
 */
public class LogConnector implements Connector {

	public final static String SYSTEM_PROPERTY_NAME_FOT_EXCLUDED_TYPES = "io.tracee.contextlogger.connector.Logconnector.excludedTypes";
	protected final static Class[] DEFAULT_EXCLUDED_TYPES = {CommonDataContextProvider.class, TraceeMdcContextProvider.class};
	private final static String SYSTEM_PROPERTY_SPLITTER_REGEX = "[, ;]";
	private final static Logger logger = LoggerFactory.getLogger(LogConnector.class);

	private final Class[] excludedTypes;

	public LogConnector() {
		this.excludedTypes = getTypesToBeExcluded();
	}

	@Override
	public void init(Map<String, String> properties) {

	}

	@Override
	public final void sendErrorReport(ConnectorOutputProvider connectorOutputProvider) {

		ConnectorOutputProvider localConnectorOutputProvider = connectorOutputProvider.excludeContextProviders(excludedTypes);

		String output = localConnectorOutputProvider.provideOutput();
		if (connectorOutputProvider instanceof LogConnectorOutputProvider) {

			LogConnectorOutputProvider logConnectorOutputProvider = (LogConnectorOutputProvider) connectorOutputProvider;
			if (logConnectorOutputProvider.getPrefix() != null) {
				output = logConnectorOutputProvider.getPrefix() + output;
			}

			switch (logConnectorOutputProvider.getLogLevel()) {
				case DEBUG:
					logger.debug(output);
					break;
				case INFO:
					logger.info(output);
					break;
				case WARN:
					logger.warn(output);
					break;
				case ERROR:
				default:
					logger.error(output);
					break;
			}

		} else {
			logger.error(output);
		}

	}

	protected Class[] getTypesToBeExcluded() {

		String excludedTypesPropertyString = System.getProperty(SYSTEM_PROPERTY_NAME_FOT_EXCLUDED_TYPES);
		if (excludedTypesPropertyString != null) {

			List<Class> typeList = new ArrayList<Class>();

			for (String className : excludedTypesPropertyString.split(SYSTEM_PROPERTY_SPLITTER_REGEX)) {
				if (!className.isEmpty()) {
					try {
						typeList.add(Class.forName(className));
					} catch (ClassNotFoundException e) {
						logger.warn("[TracEE contextlogger] - System property '" + SYSTEM_PROPERTY_NAME_FOT_EXCLUDED_TYPES
								+ "' contains nonexisting classname '" + className + "'");
					}
				}
			}

			return typeList.toArray(new Class[typeList.size()]);

		} else {
			return DEFAULT_EXCLUDED_TYPES;
		}

	}
}
