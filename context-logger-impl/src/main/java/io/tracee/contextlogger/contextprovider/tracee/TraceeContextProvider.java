package io.tracee.contextlogger.contextprovider.tracee;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.contextlogger.api.Flatten;
import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.api.ImplicitContextData;
import io.tracee.contextlogger.api.TraceeContextProviderMethod;
import io.tracee.contextlogger.contextprovider.Order;
import io.tracee.contextlogger.contextprovider.utility.NameStringValuePair;
import io.tracee.contextlogger.profile.ProfilePropertyNames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Common context data provider.
 * Created by Tobias Gindler, holisticon AG on 14.03.14.
 */
@SuppressWarnings("unused")
@io.tracee.contextlogger.api.TraceeContextProvider(displayName = "tracee", order = Order.TRACEE)
public final class TraceeContextProvider implements ImplicitContextData {

    private final TraceeBackend traceeBackend;

    public TraceeContextProvider() {
        this.traceeBackend = Tracee.getBackend();
    }

    @Override
    public ImplicitContext getImplicitContext() {
        return ImplicitContext.TRACEE;
    }

    @SuppressWarnings("unused")
    @Flatten
    @TraceeContextProviderMethod(
            displayName = "DYNAMIC",
            propertyName = ProfilePropertyNames.TRACEE,
            order = 10)
    public List<NameStringValuePair> getNameValuePairs() {

        final List<NameStringValuePair> list = new ArrayList<NameStringValuePair>();

        final Collection<String> keys = traceeBackend.keySet();
        if (keys != null) {
            for (String key : keys) {

                final String value = traceeBackend.get(key);
                list.add(new NameStringValuePair(key, value));

            }
        }
        return list.size() > 0 ? list : null;

    }

}
