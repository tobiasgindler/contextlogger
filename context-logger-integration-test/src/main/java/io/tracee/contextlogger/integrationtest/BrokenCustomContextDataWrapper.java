package io.tracee.contextlogger.integrationtest;


import io.tracee.contextlogger.api.TraceeContextProvider;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.api.WrappedContextData;

/**
 * Broken context data wrapper that throws a NullPointerException at deserialization.
 */
@TraceeContextProvider(displayName = "brokenCustomContextDataWrapper", order = 50)
public class BrokenCustomContextDataWrapper implements WrappedContextData<WrappedBrokenTestContextData> {

    public static final String PROPERTY_NAME = "brokenCustomContextDataWrapper.testOutputPropertyName";

    private WrappedBrokenTestContextData contextData;

    public BrokenCustomContextDataWrapper() {

    }

    public BrokenCustomContextDataWrapper(final WrappedBrokenTestContextData contextData) {
        this.contextData = contextData;
    }

    @Override
    public void setContextData(Object instance) throws ClassCastException {
        this.contextData = (WrappedBrokenTestContextData) instance;
    }

    @Override
    public Class<WrappedBrokenTestContextData> getWrappedType() {
        return WrappedBrokenTestContextData.class;
    }

    @SuppressWarnings("unused")
    @TraceeContextProviderMethod(
            displayName = "testoutput",
            propertyName = TestContextDataWrapper.PROPERTY_NAME,
            order = 10)
    public String getOutput() {
        throw new NullPointerException("Whoops!!!");
    }
}
