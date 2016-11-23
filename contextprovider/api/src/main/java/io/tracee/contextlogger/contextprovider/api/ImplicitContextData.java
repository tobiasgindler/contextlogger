package io.tracee.contextlogger.contextprovider.api;

/**
 * Interface to mark a class that provides implicit context data.(Class doesn't need external context information)
 */
public interface ImplicitContextData {

	/**
	 * Used to map enum values against implicit context providers.
	 * This allows adding of implicit context providers to string representation builder output just by adding the corresponding enum value.
	 *
	 * @return an enum value that implements the ImplicitContext interface
	 */
	ImplicitContext getImplicitContext();

}
