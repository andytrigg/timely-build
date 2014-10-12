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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventRecorder {
    private final Map<ExecutionEventKey, Long> startTimes = new ConcurrentHashMap<ExecutionEventKey, Long>();
    private final Map<ExecutionEventKey, Long> endTimes = new ConcurrentHashMap<ExecutionEventKey, Long>();

    public void startEvent(ExecutionEvent event) {
        startTimes.put(new ExecutionEventKey(event), System.currentTimeMillis());
    }

    public void endEvent(ExecutionEvent event) {
        endTimes.put(new ExecutionEventKey(event), System.currentTimeMillis());
    }

    public List<TimedEvent> getTimedEvents() {
        long buildStartTime = Collections.min(startTimes.values());

        List<TimedEvent> timedEvents = new ArrayList<TimedEvent>();
        for (ExecutionEventKey key : startTimes.keySet()) {
            timedEvents.add(new TimedEvent(key, startTimes.get(key) - buildStartTime, endTimes.get(key) - buildStartTime));
        }

        Collections.sort(timedEvents);

        return timedEvents;
    }

    public static class TimedEvent implements Comparable<TimedEvent> {

        private final ExecutionEventKey eventKey;
        private final Long startTime;
        private final Long endTime;

        public TimedEvent(ExecutionEventKey eventKey, long startTime, long endTime) {
            this.eventKey = eventKey;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(TimedEvent other) {
            return startTime.compareTo(other.startTime);
        }
    }
}
