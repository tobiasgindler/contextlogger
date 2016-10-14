package io.tracee.contextlogger.agent.listeners;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.utility.JavaModule;


@Slf4j
public class TransformationErrorLoggingListener extends AgentBuilder.Listener.Adapter {

	@Override
	public void onError(String typeName, ClassLoader classLoader, JavaModule javaModule, Throwable throwable) {
		log.warn("ERROR on transformation " + typeName, throwable);
	}

}
