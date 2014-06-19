package io.tracee.contextlogger.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes that can be processed by a context toJson provider implementation
 * ( for example the TraceeGenericGsonSerializer).
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface TraceeContextLogProvider {

    String displayName();

    int order() default 100;

}
