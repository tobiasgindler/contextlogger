package io.tracee.contextlogger.agent.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


@Getter
@Setter
@Slf4j
public class InstrumentationConfig {

	private final static String DOUBLE_WILDCARD_PATTERN = "[a-zA-Z0-9.]*";
	private final static String SINGLE_WILDCARD_PATTERN = "[a-zA-Z0-9]*";


	private static Pattern wildcardPattern;

	private final String classNameRegex;
	private final String methodNameRegex;

	private final boolean useClassNamePattern;
	private final boolean useMethodNamePattern;


	private final Pattern classNamePattern;
	private final Pattern methodNamePattern;

	public InstrumentationConfig(String classNameWildcardString, String methodNameWildcardString) {

		this.classNameRegex = convertDoubleWildcardStringToRegex(classNameWildcardString);
		this.methodNameRegex = convertSingleWildcardStringToRegex(methodNameWildcardString);


		Pattern tmpClassNamePattern = null;
		if (classNameRegex != null && !classNameRegex.isEmpty()) {

			try {
				tmpClassNamePattern = Pattern.compile(classNameRegex);
			} catch (PatternSyntaxException e) {
				log.warn("Cannot compile pattern for classname regex : {}", classNameRegex);
			}

		}
		this.classNamePattern = tmpClassNamePattern;
		this.useClassNamePattern = (this.classNamePattern != null);

		Pattern tmpMethodNamePattern = null;
		if (methodNameRegex != null && !methodNameRegex.isEmpty()) {
			try {
				tmpMethodNamePattern = Pattern.compile(methodNameRegex);
			} catch (PatternSyntaxException e) {
				log.warn("Cannot compile pattern for method regex : {}", methodNameRegex);
			}

		}
		this.methodNamePattern = tmpMethodNamePattern;
		this.useMethodNamePattern = (this.methodNamePattern != null);

	}


	public boolean matches(String className, String methodName) {
		return classMatches(className) && methodMatches(methodName);
	}

	public boolean classMatches(String className) {

		return matchesPattern(classNamePattern, className);

	}

	public boolean methodMatches(String methodName) {
		return matchesPattern(methodNamePattern, methodName);
	}


	protected boolean matchesPattern(Pattern pattern, String valueToCheck) {


		return pattern == null || (valueToCheck != null && pattern.matcher(valueToCheck).matches());


	}


	// TODO: This should be done in a cleaner way
	protected static String convertDoubleWildcardStringToRegex(String wildcardString) {

		if (wildcardString == null || wildcardString.isEmpty()) {
			return null;
		}

		// init pattern if necessary
		if (wildcardPattern == null) {
			wildcardPattern = Pattern.compile("[a-zA-Z0-9.*]*");
		}

		if (wildcardPattern.matcher(wildcardString).matches()) {


			StringBuilder regexBuilder = new StringBuilder();


			String convertedWildcardString = wildcardString.replaceAll("[.]", "[.]");

			// first process double wildcards
			boolean isFirst = true;
			String result = "";

			// need to catch prefix and suffix ** cases
			boolean startsWithWildcard = false;
			boolean endswithWildcard = false;
			if (convertedWildcardString.startsWith("**")) {
				convertedWildcardString = convertedWildcardString.substring(2);
				startsWithWildcard = true;
			}
			if (convertedWildcardString.endsWith("**")) {
				convertedWildcardString = convertedWildcardString.substring(0, convertedWildcardString.length() - 2);
				endswithWildcard = true;
			}

			for (String element : convertedWildcardString.split("[*]{2}")) {
				if (!isFirst) {
					result += DOUBLE_WILDCARD_PATTERN;
				} else {
					isFirst = false;
				}

				result += convertSingleWildcardStringToRegex(element);


			}

			convertedWildcardString = (startsWithWildcard ? DOUBLE_WILDCARD_PATTERN : "") + result + (endswithWildcard ? DOUBLE_WILDCARD_PATTERN : "");


			return convertedWildcardString;
		} else {
			return null;
		}
	}


	protected static String convertSingleWildcardStringToRegex(String wildcardString) {
		if (wildcardString == null) {
			return null;
		}

		return wildcardString.replaceAll("[*]", SINGLE_WILDCARD_PATTERN);

	}


}
