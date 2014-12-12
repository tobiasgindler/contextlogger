package io.tracee.contextlogger.integritycheck;

import io.tracee.contextlogger.api.Flatten;
import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.utility.NameObjectValuePair;
import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import io.tracee.contextlogger.testdata.AnnotationTestClass;
import io.tracee.contextlogger.testdata.TestClassWithMethods;
import io.tracee.contextlogger.utility.TraceeContextLogAnnotationUtilities;
import org.junit.Assert;
import org.junit.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Does a integrity check for all (@link Flatten), {@link io.tracee.contextlogger.api.TraceeContextProvider}
 * and {@link io.tracee.contextlogger.api.TraceeContextProviderMethod} annotated  classes and methods.
 * Created by Tobias Gindler, holisticon AG on 18.03.14.
 */
public class TraceeContextLogMethodAnnotationIntegrityCheck {

    private Set<Class> ignoredTestClasses = new HashSet<Class>();
    {
        ignoredTestClasses.add(AnnotationTestClass.class);
        ignoredTestClasses.add(TestClassWithMethods.class);

    }

    @Test
    public void checkIntegrity () {

        Reflections reflections = new Reflections("io.tracee.contextlogger");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TraceeContextProvider.class);

        for (Class clazz : annotated) {

            if (ignoredTestClasses.contains(clazz)) {
                continue;
            }

            for (Method method:clazz.getDeclaredMethods()) {

                boolean isTraceeContextLogMethodAnnotationSet = method.getAnnotation(TraceeContextProviderMethod.class) != null;
                boolean isFlattenAnnotationSet = method.getAnnotation(Flatten.class) != null;
                if (isTraceeContextLogMethodAnnotationSet) {

                    // check for public method
                    if (!TraceeContextLogAnnotationUtilities.checkIsPublic(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method is not public.");
                    }

                    if (!TraceeContextLogAnnotationUtilities.checkMethodHasNoParameters(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method has more than one parameter.");
                    }

                    if (!TraceeContextLogAnnotationUtilities.checkMethodHasNonVoidReturnType(method)) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " method has void as return type.");
                    }

                    if (isFlattenAnnotationSet && (!List.class.isAssignableFrom(method.getReturnType()) || ( !method.getGenericReturnType().toString().contains(NameStringValuePair.class.getCanonicalName()) && !method.getGenericReturnType().toString().contains(NameObjectValuePair.class.getCanonicalName())) ) ) {
                        Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " Method annotated with Flatten but has no return type of List<NameStringValuePair>.");
                    }

                } else if (isFlattenAnnotationSet){
                    Assert.fail(clazz.getCanonicalName() + "." + method.getName() + " Flatten Annotation is set without TraceeContextProviderMethod Method.");
                }




            }

        }


    }




}
