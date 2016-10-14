package io.tracee.contextlogger.agent.configuration;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class InstrumentationConfigLoader {

	public final static String DEFAULT_SETTING_FILENAME = "contextloggerSettings.json";

	public final static String SETTING_IGNORED_PACKAGES = "ignoredPackages";
	public final static String SETTING_ACTIVATED_METRICS = "activatedMetrics";
	public final static String SETTING_ACTIVATED_CONTEXTLOGGER = "activatedContextlogger";

	public final static String SETTING_PARAMETER_NAME_FQN = "fqn";
	public final static String SETTING_PARAMETER_NAME_METHOD = "method";

	public InstrumentationConfig[] contextLoggerConfig;
	public InstrumentationConfig[] metricsConfig;
	public String[] deactivatedPackages;

	public static InstrumentationConfigLoader INSTANCE = new InstrumentationConfigLoader();


	private InstrumentationConfigLoader() {

		try {

			log.info("Try to read configuration from file.");

			String jsonStr = readFile(DEFAULT_SETTING_FILENAME);

			log.info("Read the following JSON configuration from file : {}", jsonStr);
			SimpleJsonParser.Obj jsonObject = (SimpleJsonParser.Obj) SimpleJsonParser.parse(jsonStr);

			deactivatedPackages = getIgnoredPackagePrefixes(jsonObject);
			contextLoggerConfig = getInstrumentationConfig(jsonObject, SETTING_ACTIVATED_CONTEXTLOGGER);
			metricsConfig = getInstrumentationConfig(jsonObject, SETTING_ACTIVATED_METRICS);


		} catch (Exception e) {
			log.error("Can't read configuration file '{}'", DEFAULT_SETTING_FILENAME, e);
		}

	}


	private static String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	private static String[] getIgnoredPackagePrefixes(SimpleJsonParser.Obj jsonObject) {
		// read deactivated packages
		SimpleJsonParser.Array ignoredPackagesArray = (SimpleJsonParser.Array) jsonObject.getValueByKey(SETTING_IGNORED_PACKAGES);
		List<String> ignoredPackagesList = new ArrayList();

		log.info("Ignoring the following packages during instrumentation:");

		for (int i = 0; i < ignoredPackagesArray.getSize(); i++) {
			String packageToBeIgnored = ((SimpleJsonParser.Text) ignoredPackagesArray.getValue(i)).getValue();
			log.info("[{}] := {}", i, packageToBeIgnored);
			ignoredPackagesList.add(packageToBeIgnored);
		}

		return ignoredPackagesList.toArray(new String[ignoredPackagesList.size()]);
	}


	private static InstrumentationConfig[] getInstrumentationConfig(SimpleJsonParser.Obj jsonObject, String config) {

		List<InstrumentationConfig> list = new ArrayList<InstrumentationConfig>();


		if (jsonObject != null) {

			SimpleJsonParser.Array array = (SimpleJsonParser.Array) jsonObject.getValueByKey(config);

			if (array != null) {

				for (int i = 0; i < array.getSize(); i++) {

					SimpleJsonParser.Obj element = (SimpleJsonParser.Obj) array.getValue(i);

					SimpleJsonParser.Text fqn = (SimpleJsonParser.Text) element.getValueByKey(SETTING_PARAMETER_NAME_FQN);
					SimpleJsonParser.Text method = (SimpleJsonParser.Text) element.getValueByKey(SETTING_PARAMETER_NAME_METHOD);

					list.add(new InstrumentationConfig(fqn != null ? fqn.getValue() : null, method != null ? method.getValue() : null));
				}


			}


		}


		return list.toArray(new InstrumentationConfig[list.size()]);

	}


	public static InstrumentationConfigLoader getInstance() {
		return INSTANCE;
	}

}
