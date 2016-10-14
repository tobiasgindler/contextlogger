package io.tracee.contextlogger.contextprovider.agent;

import java.lang.reflect.Method;


public class AgentDataWrapper {

	private final Class clazz;
	private final Method method;
	private final Object[] parameters;

	public AgentDataWrapper (Class clazz, Method method, Object[] parameters) {
		this.clazz = clazz;
		this.method = method;
		this.parameters = parameters;
	}

	public static AgentDataWrapper wrap(Class clazz, Method method, Object[] parameters) {
		return new AgentDataWrapper(clazz, method, parameters);
	}

	public Class getClazz() {
		return clazz;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getParameters() {
		return parameters;
	}
}
