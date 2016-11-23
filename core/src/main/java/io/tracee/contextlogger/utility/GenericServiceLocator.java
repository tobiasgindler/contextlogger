package io.tracee.contextlogger.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A generic service locator.
 */
public class GenericServiceLocator {

	private static final Logger logger = LoggerFactory.getLogger(GenericServiceLocator.class);

	private GenericServiceLocator() {
	}

	public static <T> T locate(final Class<T> clazz) {
		final List services = locateAll(clazz);
		return services.isEmpty() ? (T) null : (T) services.get(0);
	}

	public static <T> List<T> locateAll(final Class<T> clazz) {

		final Iterator<T> iterator = ServiceLoader.load(clazz).iterator();
		List<T> services = new ArrayList<T>();

		while (iterator.hasNext()) {
			try {
				services.add(iterator.next());
			} catch (Error e) {
				logger.error("Error ocurred during loading of service providers.", e);
			}
		}

		logger.error("CHECK AND LOAD CSP VIA FALLBACK : {}", services.isEmpty());
		for (T service : services) {
			logger.info("fqn:{}", service.getClass().getCanonicalName());
		}
		//if (services.isEmpty()) {
		services.addAll(fallback(clazz));
		//}

		return services;

	}


	private static <T> List<T> fallback(final Class<T> clazz) {

		logger.info("try to get context provider service provider manually.");

		final List<T> services = new ArrayList<T>();

		InputStream inputStream = null;
		BufferedReader br = null;
		try {
			inputStream = GenericServiceLocator.class.getResourceAsStream("/META-INF/services/" + clazz.getCanonicalName());
			br = new BufferedReader(new InputStreamReader(inputStream));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				logger.info("check line {}", line);
				line = line.trim();
				if (!line.isEmpty()) {
					try {
						Class serviceProviderCandidate = Class.forName(line);

						if (clazz.isAssignableFrom(serviceProviderCandidate)) {
							T serviceProviderInstance = (T) serviceProviderCandidate.newInstance();
							services.add(serviceProviderInstance);
						} else {
							logger.warn("Found context service provider class definition that isn't compatible with the service provider interface");
						}

					} catch (Exception e) {
						logger.info("Couldn't get load class or create instance provider service provider for class name {}.", line, e);
					}
				}
			}


		} catch (Exception e) {
			logger.info("Couldn't get context provider service provider manually.", e);

		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return services;

	}

}
