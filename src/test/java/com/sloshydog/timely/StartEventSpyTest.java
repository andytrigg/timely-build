/**
 * Copyright (c) 2014. gigantiqandy@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sloshydog.timely;

import org.apache.maven.eventspy.EventSpy;
import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.maven.execution.ExecutionEvent.Type.MojoStarted;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class StartEventSpyTest {

    private final ExecutionEvent.Type[] spiedEvents = new ExecutionEvent.Type[]{MojoStarted};

    @Test
    public void shouldBeAnEventSpy() {
        assertThat(EventSpy.class).isAssignableFrom(StartEventSpy.class);
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(StartEventSpy.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldRecordAStartEventForAllSpiedEvents() throws Exception {
        List<ExecutionEvent.Type> spiedEventTypes = asList(spiedEvents);

        StartEventSpy StartEventSpy = spy(new StartEventSpy());
        EventRecorder eventRecorder = mock(EventRecorder.class);
        doReturn(eventRecorder).when(StartEventSpy).getEventRecorder();

        for (ExecutionEvent.Type eventTypeToBeSpied : spiedEventTypes) {
            ExecutionEvent eventToBeSpied = mockEventFor(eventTypeToBeSpied);
            StartEventSpy.onEvent(eventToBeSpied);
            verify(eventRecorder).startEvent(eventToBeSpied);
        }

        verifyNoMoreInteractions(eventRecorder);
    }

    @Test
    public void shouldIgnoreAllOtherEvents() throws Exception {
        List<ExecutionEvent.Type> eventsIgnoredBySpy = new ArrayList(asList(ExecutionEvent.Type.values()));
        eventsIgnoredBySpy.removeAll(asList(spiedEvents));

        StartEventSpy StartEventSpy = spy(new StartEventSpy());
        EventRecorder eventRecorder = mock(EventRecorder.class);
        doReturn(eventRecorder).when(StartEventSpy).getEventRecorder();

        for (ExecutionEvent.Type eventTypeIgnored : eventsIgnoredBySpy) {
            ExecutionEvent eventToBeIgnored = mockEventFor(eventTypeIgnored);
            StartEventSpy.onEvent(eventToBeIgnored);
        }

        verifyZeroInteractions(eventRecorder);
    }

    private ExecutionEvent mockEventFor(ExecutionEvent.Type event) {
        ExecutionEvent executionEvent = mock(ExecutionEvent.class);
        when(executionEvent.getType()).thenReturn(event);
        return executionEvent;
    }
}
