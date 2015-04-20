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
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.maven.execution.ExecutionEvent.Type.SessionEnded;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EndSessionSpyTest {

    private final ExecutionEvent.Type[] spiedEvents = new ExecutionEvent.Type[]{SessionEnded};

    @Test
    public void shouldBeAnEventSpy() {
        assertThat(EventSpy.class).isAssignableFrom(EndSessionSpy.class);
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(EndSessionSpy.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldGenerateAReportForAllSpiedEvents() throws Exception {
        List<ExecutionEvent.Type> spiedEventTypes = asList(spiedEvents);

        EndSessionSpy endSessionSpy = spy(new EndSessionSpy());
        EventRecorder eventRecorder = mock(EventRecorder.class);
        ReportGenerator reportGenerator = mock(ReportGenerator.class);
        doReturn(eventRecorder).when(endSessionSpy).getEventRecorder();
        doReturn(reportGenerator).when(endSessionSpy).getReportGenerator();
        List<EventRecorder.TimedEvent> timedEvents = new ArrayList<EventRecorder.TimedEvent>();
        when(eventRecorder.getTimedEvents()).thenReturn(timedEvents);

        for (ExecutionEvent.Type eventTypeToBeSpied : spiedEventTypes) {
            MavenSession mavenSession = mock(MavenSession.class);
            ExecutionEvent eventToBeSpied = mockEventFor(mavenSession, eventTypeToBeSpied);
            endSessionSpy.onEvent(eventToBeSpied);

            verify(reportGenerator).createReportFor(mavenSession, timedEvents);;
        }
    }

    @Test
    public void shouldIgnoreAllOtherEvents() throws Exception {
        List<ExecutionEvent.Type> eventsIgnoredBySpy = new ArrayList(asList(ExecutionEvent.Type.values()));
        eventsIgnoredBySpy.removeAll(asList(spiedEvents));

        EndSessionSpy endSessionSpy = spy(new EndSessionSpy());
        EventRecorder eventRecorder = mock(EventRecorder.class);
        ReportGenerator reportGenerator = mock(ReportGenerator.class);
        doReturn(eventRecorder).when(endSessionSpy).getEventRecorder();
        doReturn(reportGenerator).when(endSessionSpy).getReportGenerator();

        for (ExecutionEvent.Type eventTypeIgnored : eventsIgnoredBySpy) {
            ExecutionEvent eventToBeIgnored = mockEventFor(mock(MavenSession.class), eventTypeIgnored);
            endSessionSpy.onEvent(eventToBeIgnored);
        }

        verifyZeroInteractions(eventRecorder, reportGenerator);
    }

    private ExecutionEvent mockEventFor(MavenSession mavenSession, ExecutionEvent.Type event) {
        ExecutionEvent executionEvent = mock(ExecutionEvent.class);
        when(executionEvent.getSession()).thenReturn(mavenSession);
        when(executionEvent.getType()).thenReturn(event);
        return executionEvent;
    }
}
