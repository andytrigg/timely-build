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

import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Component(role = EventRecorder.class)
public class EventRecorder {
    @Requirement
    private TimeSource timeSource;
    @Requirement
    private ExecutionEventKeyFactory executionEventKeyFactory;

    private final Map<ExecutionEventKey, Long> startTimes = new ConcurrentHashMap<ExecutionEventKey, Long>();
    private final Map<ExecutionEventKey, Long> endTimes = new ConcurrentHashMap<ExecutionEventKey, Long>();

    public void startEvent(ExecutionEvent event) {
        startTimes.put(getExecutionEventKeyFactory().create(event), getTimeSource().currentSystemTime());
    }

    public void endEvent(ExecutionEvent event) {
        endTimes.put(getExecutionEventKeyFactory().create(event), getTimeSource().currentSystemTime());
    }

    TimeSource getTimeSource() {
        return timeSource;
    }

    ExecutionEventKeyFactory getExecutionEventKeyFactory() {
        return executionEventKeyFactory;
    }

    public List<TimedEvent> getTimedEvents() {
        long buildStartTime = Collections.min(startTimes.values());

        List<TimedEvent> timedEvents = new ArrayList<TimedEvent>();
        for (ExecutionEventKey key : startTimes.keySet()) {
            if (!endTimes.containsKey(key)) {
                throw new IllegalStateException(format("No end time recorded for event '%s'", key.toString()));
            }
            timedEvents.add(new TimedEvent(key, startTimes.get(key) - buildStartTime, endTimes.get(key) - buildStartTime));
        }

        Collections.sort(timedEvents);

        return timedEvents;
    }

    public static class TimedEvent implements Comparable<TimedEvent> {

        String group;
        String goal;
        String phase;
        String project;
        Long startTime;
        Long endTime;

        public TimedEvent(ExecutionEventKey eventKey, long startTime, long endTime) {
            this.group = eventKey.getGroup();
            this.goal = eventKey.getGoal();
            this.phase = eventKey.getPhase();
            this.project = eventKey.getProject();
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int hashCode() {
            return reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return reflectionEquals(this, obj);
        }

        @Override
        public String toString() {
            return reflectionToString(this);
        }

        @Override
        public int compareTo(TimedEvent other) {
            return startTime.compareTo(other.startTime);
        }
    }
}
