package io.tracee.contextlogger.connector;

import java.util.Map;

/**
 * A global interface used to send context information.
 */
public interface Connector {

	void init(Map<String, String> properties);

	void sendErrorReport(ConnectorOutputProvider connectorOutputProvider);

}
