package io.tracee.contextlogger.contextprovider.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark methods that provide context information for context logging.
 * This annotation must reside in classes that are annotated with the
 * {@link TraceeContextProvider} annotation.
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
public @interface TraceeContextProviderMethod {

	String displayName();

	boolean enabledPerDefault() default false;

	int order() default 0;


}
