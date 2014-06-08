package io.tracee.contextlogger.builder.gson;

import io.tracee.contextlogger.RegexMatcher;
import io.tracee.contextlogger.data.subdata.java.JavaThrowableContextProvider;
import io.tracee.contextlogger.data.subdata.tracee.PassedDataContextProvider;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for {@link io.tracee.contextlogger.builder.gson.TraceeGsonContextLogBuilder}.
 * Created by Tobias Gindler on 20.03.14.
 */
public class TraceeGsonContextLogBuilderTest {

	@Test
	public void should_create_context_log() {
		final TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
		logBuilder.setWrapperClasses(new HashSet<Class>(Arrays.<Class>asList(JavaThrowableContextProvider.class)));

		String json = logBuilder.log(new JavaThrowableContextProvider(new NullPointerException()));

		assertThat(json, RegexMatcher.matches("\\[\\{\\\"stacktrace\\\":\\\"java.lang.Null.*\\}\\]"));
	}

	@Test
	public void should_create_mixed_context_log() {
		TraceeGsonContextLogBuilder logBuilder = new TraceeGsonContextLogBuilder();
		Set<Class> classes = new HashSet<Class>();
		classes.add(PassedDataContextProvider.class);
		classes.add(JavaThrowableContextProvider.class);

		logBuilder.setWrapperClasses(classes);

		Object[] oarray = new Object[2];
		oarray[0] = new JavaThrowableContextProvider(new NullPointerException());
		oarray[1] = "TATA";
		PassedDataContextProvider cp = new PassedDataContextProvider(oarray);

		String json = logBuilder.log(cp);

		assertThat(json, RegexMatcher.matches("\\[\\{\\\"throwable\\\":\\{\\\"stacktrace\\\":\\\"java.lang.NullPointerException.*java.lang.String\\\":\\\"TATA\\\"\\}\\]"));
	}
}