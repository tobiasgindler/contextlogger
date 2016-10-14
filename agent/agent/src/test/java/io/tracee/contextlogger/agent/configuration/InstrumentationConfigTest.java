package io.tracee.contextlogger.agent.configuration;


import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(value = Parameterized.class)
public class InstrumentationConfigTest {

	private String classWildcardString;
	private String methodWildcardString;
	private String testcaseClassName;
	private String testcaseMethodName;
	private boolean matches;


	public InstrumentationConfigTest(
			String classWildcardString,
			String methodWildcardString,
			String testcaseClassName,
			String testcaseMethodName,
			boolean matches) {

		this.classWildcardString = classWildcardString;
		this.methodWildcardString = methodWildcardString;
		this.testcaseClassName = testcaseClassName;
		this.testcaseMethodName = testcaseMethodName;
		this.matches = matches;

	}


	@Parameterized.Parameters(name = "{index}: test Config (\"{0}\",\"{1}\") to regex conversion. testcase (\"{2}\",\"{3}\") should match: {4}")
	public static List<Object[]> data() {

		return Arrays.asList(new Object[][]{
				{"a**ij", "*es*", "abc.def.hij", "test", true},
				{"**ij", "te*", "abc.def.hij", "test", true},
				{"a**", "*st", "abc.def.hij", "test", true},
				{"abc.def.hij", "test", "abc.def.hij", "test", true},
				{"abc.def.hij", "test", "abc.def.hijX", "test", false},
				{"abc.def.hij", "test", "abc.def.hij", "testX", false},
				{null, "test", "abc.def.hij", "test", true},
				{"abc.def.hij", null, "abc.def.hij", "test", true},
				{null, null, "abc.def.hij", "test", true}


		});

	}


	@Test
	public void testConversionOfWildcardsToRegex() {

		InstrumentationConfig unit = new InstrumentationConfig(classWildcardString, methodWildcardString);

		MatcherAssert.assertThat(unit.matches(testcaseClassName, testcaseMethodName), Matchers.is(matches));


	}


}
