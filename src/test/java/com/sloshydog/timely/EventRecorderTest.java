package com.sloshydog.timely;

import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventRecorderTest {

    private static final java.lang.Long NOW = 12345L;
    @Mock
    private TimeSource timeSource = mock(TimeSource.class);
    @Mock
    private ExecutionEventKeyFactory executionEventKeyFactory;
    private EventRecorder eventRecorder;

    @Before
    public void setUp() throws Exception {
        eventRecorder = spy(new EventRecorder());

        doReturn(timeSource).when(eventRecorder).getTimeSource();
        doReturn(executionEventKeyFactory).when(eventRecorder).getExecutionEventKeyFactory();
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(EventRecorder.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldThrowAMeaningfulExceptionWhenAnEventIsNotEnded() {
        when(timeSource.currentSystemTime()).thenReturn(NOW);

        ExecutionEventKey executionEventKey = createMockExecutionEventKey("build step");

        ExecutionEvent event = mock(ExecutionEvent.class);
        when(executionEventKeyFactory.create(event)).thenReturn(executionEventKey);

        eventRecorder.startEvent(event);
        try {
            eventRecorder.getTimedEvents();
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException e) {
            assertThat(e).hasMessage("No end time recorded for event 'build step'");
        }
    }

    @Test
    public void shouldBeAbleToGetTheSortedTimedEvents() {
        ExecutionEvent event1 = mock(ExecutionEvent.class);
        ExecutionEvent event2 = mock(ExecutionEvent.class);
        ExecutionEvent event3 = mock(ExecutionEvent.class);

        ExecutionEventKey executionEventKey1 = createMockExecutionEventKey("event1");
        ExecutionEventKey executionEventKey2 = createMockExecutionEventKey("event2");
        ExecutionEventKey executionEventKey3 = createMockExecutionEventKey("event3");

        when(executionEventKeyFactory.create(event1)).thenReturn(executionEventKey1);
        when(executionEventKeyFactory.create(event2)).thenReturn(executionEventKey2);
        when(executionEventKeyFactory.create(event3)).thenReturn(executionEventKey3);

        when(timeSource.currentSystemTime()).thenReturn(NOW).thenReturn(NOW + 100).thenReturn(NOW + 200).thenReturn(NOW + 300).thenReturn(NOW + 100).thenReturn(NOW + 200);

        eventRecorder.startEvent(event1);
        eventRecorder.endEvent(event1);
        eventRecorder.startEvent(event3);
        eventRecorder.endEvent(event3);
        eventRecorder.startEvent(event2);
        eventRecorder.endEvent(event2);


        List<EventRecorder.TimedEvent> timedEvents = eventRecorder.getTimedEvents();

        assertThat(timedEvents).contains(new EventRecorder.TimedEvent(executionEventKey1, 0, 100), new EventRecorder.TimedEvent(executionEventKey2, 100, 200), new EventRecorder.TimedEvent(executionEventKey3, 200, 300));
    }

    private ExecutionEventKey createMockExecutionEventKey(String eventName) {
        ExecutionEventKey executionEventKey = mock(ExecutionEventKey.class);
        when(executionEventKey.getGoal()).thenReturn(eventName + "goal");
        when(executionEventKey.getGroup()).thenReturn(eventName + "group");
        when(executionEventKey.getPhase()).thenReturn(eventName + "phase");
        when(executionEventKey.getProject()).thenReturn(eventName + "project");
        when(executionEventKey.toString()).thenReturn(eventName);
        return executionEventKey;
    }
}