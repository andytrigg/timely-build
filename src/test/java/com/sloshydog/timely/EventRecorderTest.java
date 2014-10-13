package com.sloshydog.timely;

import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventRecorderTest {

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(EventRecorder.class).hasAnnotation(Component.class);
    }
}