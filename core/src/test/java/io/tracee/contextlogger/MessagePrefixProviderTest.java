package io.tracee.contextlogger;

import io.tracee.contextlogger.connector.LogLevel;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test class for {@link io.tracee.contextlogger.MessagePrefixProvider}.
 */
public class MessagePrefixProviderTest {

	@Test
	public void provide_prefix_with_info_log_level() {
		LogLevel givenLogLevel = LogLevel.INFO;

		String prefix = MessagePrefixProvider.provideLogMessagePrefix(givenLogLevel, MessagePrefixProviderTest.class);
		MatcherAssert.assertThat(prefix, Matchers.is("TRACEE_CL_" + givenLogLevel.name() + "[MessagePrefixProviderTest]  : "));

	}

	@Test
	public void provide_prefix_with_default_log_level() {
		LogLevel givenLogLevel = null;

		String prefix = MessagePrefixProvider.provideLogMessagePrefix(givenLogLevel, MessagePrefixProviderTest.class);
		MatcherAssert.assertThat(prefix, Matchers.is("TRACEE_CL_" + MessagePrefixProvider.DEFAULT_LEVEL.name() + "[MessagePrefixProviderTest]  : "));

	}

	@Test
	public void provide_prefix_with_default_log_level_and_no_type() {
		LogLevel givenLogLevel = null;

		String prefix = MessagePrefixProvider.provideLogMessagePrefix(givenLogLevel, (Class) null);
		MatcherAssert.assertThat(prefix, Matchers.is("TRACEE_CL_" + MessagePrefixProvider.DEFAULT_LEVEL.name() + "[]  : "));

	}

}
