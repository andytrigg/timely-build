package com.sloshydog.timely;

import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class TimelyBuildExecutionListenerFactoryTest {

    @Test
    public void shouldBeAnExecutionListenerFactory() {
        assertThat(ExecutionListenerFactory.class).isAssignableFrom(TimelyBuildExecutionListenerFactory.class);
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(TimelyBuildExecutionListenerFactory.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldBeAbleToCreateAExecutionListener() {
        MavenSession mavenSession = mock(MavenSession.class);

        assertThat(new TimelyBuildExecutionListenerFactory().create(mavenSession)).isNotNull();
    }
}