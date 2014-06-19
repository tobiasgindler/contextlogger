package io.tracee.contextlogger.impl;

import io.tracee.contextlogger.api.ImplicitContext;
import io.tracee.contextlogger.contextprovider.java.JavaThrowableContextProvider;
import io.tracee.contextlogger.contextprovider.servlet.ServletRequestContextProvider;
import io.tracee.contextlogger.profile.Profile;
import io.tracee.contextlogger.profile.ProfilePropertyNames;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * Test class for {@link ContextLoggerConfiguration}.
 * Created by Tobias Gindler, holisticon AG on 01.04.14.
 */
public class ContextLoggerConfigurationTest {

    @Before
    public void init() {
        System.setProperty(ProfilePropertyNames.PROFILE_SET_GLOBALLY_VIA_SYSTEM_PROPERTIES, Profile.BASIC.name());
    }


    @Test
    public void should_get_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());

    }

    @Test
    public void should_get_implicit_wrapper_list_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap(), Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap().containsKey(ImplicitContext.COMMON), Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getImplicitContextClassMap().containsKey(ImplicitContext.TRACEE), Matchers.equalTo(true));

    }

    @Test
    public void should_get_wrapper_class_mapping_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap(), Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap().containsKey(HttpServletRequest.class), Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getClassToWrapperMap().get(HttpServletRequest.class).equals(ServletRequestContextProvider.class), Matchers.equalTo(true));

    }


    @Test
    @Ignore
    public void should_get_profile_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getProfile(), Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getProfile(), Matchers.equalTo(Profile.BASIC));

    }


    @Test
    public void should_get_wrapper_classes_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses(), Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses().contains(ServletRequestContextProvider.class), Matchers.equalTo(true));
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperClasses().contains(JavaThrowableContextProvider.class), Matchers.equalTo(true));

    }


    @Test
    public void should_get_wrappers_of_context_logger_configuration() {

        ContextLoggerConfiguration contextLoggerConfiguration = ContextLoggerConfiguration.getOrCreateContextLoggerConfiguration();

        MatcherAssert.assertThat(contextLoggerConfiguration, Matchers.notNullValue());

        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperList(), Matchers.notNullValue());
        MatcherAssert.assertThat(contextLoggerConfiguration.getWrapperList().size(), Matchers.greaterThan(0));
    }

}
