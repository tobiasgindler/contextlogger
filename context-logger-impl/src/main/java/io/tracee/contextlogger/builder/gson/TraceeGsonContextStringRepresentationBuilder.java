package io.tracee.contextlogger.builder.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.tracee.contextlogger.builder.AbstractContextStringRepresentationBuilder;
import io.tracee.contextlogger.contextprovider.tracee.PassedDataContextProvider;
import io.tracee.contextlogger.profile.ProfileSettings;

/**
 * Context Logger implementation for gson.
 * Created by Tobias Gindler, holisticon AG on 20.03.14.
 */
public class TraceeGsonContextStringRepresentationBuilder extends AbstractContextStringRepresentationBuilder {

	private Gson gson = null;

	/**
	 * Gets or creates the gson instance generation of output.
	 */
	private Gson getOrCreateGson() {

		if (gson == null) {
			final GsonBuilder gsonBuilder = new GsonBuilder();

			final TraceeGenericGsonSerializer gsonSerializer = new TraceeGenericGsonSerializer(new ProfileSettings(this.getProfile(),
					this.getManualContextOverrides()));

			for (Class clazz : this.getWrapperClasses()) {
				gsonBuilder.registerTypeAdapter(clazz, gsonSerializer);
			}

			gson = gsonBuilder.create();
		}

		return gson;
	}


	@Override
	public final String createStringRepresentation(Object... instancesToLog) {
		return getOrCreateGson().toJson(instancesToLog);
	}

	@Override
	public final String createStringRepresentationForPassedDataContextProvider(PassedDataContextProvider passedContextData) {
		return getOrCreateGson().toJson(passedContextData);
	}
}
