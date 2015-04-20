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

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.ExecutionEvent;
import org.codehaus.plexus.component.annotations.Requirement;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.addAll;

public abstract class AbstractTimelyEventSpy extends AbstractEventSpy {
    @Requirement
    private EventRecorder eventRecorder;

    private final Set<ExecutionEvent.Type> events = new HashSet<ExecutionEvent.Type>();

    public AbstractTimelyEventSpy(ExecutionEvent.Type ... eventTypes) {
        addAll(events, eventTypes);
    }

    @Override
    public void onEvent(Object event) throws Exception {
        if (event instanceof ExecutionEvent && events.contains(((ExecutionEvent) event).getType())) {

            doOnEvent((ExecutionEvent) event);
        }
    }

    protected abstract void doOnEvent(ExecutionEvent event);
}
