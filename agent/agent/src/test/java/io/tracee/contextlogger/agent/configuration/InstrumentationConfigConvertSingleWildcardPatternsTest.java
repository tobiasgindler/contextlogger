package io.tracee.contextlogger.agent.configuration;

import io.tracee.contextlogger.util.test.RegexMatcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(value = Parameterized.class)
public class InstrumentationConfigConvertSingleWildcardPatternsTest {


	private String wildcardString;
	private String testcase;
	private boolean matches;


	public InstrumentationConfigConvertSingleWildcardPatternsTest(String testcase, String wildcardString, boolean matches) {
		this.wildcardString = wildcardString;
		this.testcase = testcase;
		this.matches = matches;
	}


	@Parameterized.Parameters(name = "{index}: test wildcard \"{1}\" to regex conversion. testcase \"{0}\" should match: {2}")
	public static List<Object[]> data() {

		return Arrays.asList(new Object[][]{
				{"abcdef", "ab*ef", true},
				{"abcdef", "*", true},
				{"abcdef", "*def", true},
				{"abcdef", "*d", false},
				{"abcdef", "abc*", true},
				{"abcdef", "d*", false},
				{"abcdef", "abcdef", true},
				{"abcdef", "dsfsdf", false},
				{"abcdef", "dsfsdf", false},
				{"test", "te*", true}
		});

	}

	@Test
	public void testConversionOfWildcardsToRegex() {

		InstrumentationConfig unit = new InstrumentationConfig("", "");

		if (matches) {
			MatcherAssert.assertThat(testcase, RegexMatcher.matches(InstrumentationConfig.convertDoubleWildcardStringToRegex(wildcardString)));
		} else {
			MatcherAssert.assertThat(testcase, Matchers.not(RegexMatcher.matches(InstrumentationConfig.convertDoubleWildcardStringToRegex(wildcardString))));
		}


	}


}
