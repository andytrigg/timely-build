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

import static org.apache.maven.execution.ExecutionEvent.Type.MojoFailed;
import static org.apache.maven.execution.ExecutionEvent.Type.MojoSkipped;
import static org.apache.maven.execution.ExecutionEvent.Type.MojoSucceeded;

@Component(role = EventSpy.class, hint = "endeventspy")
public class EndEventSpy extends AbstractTimelyEventSpy {

    public EndEventSpy() {
        super(MojoSkipped, MojoSucceeded, MojoFailed);
    }

    @Override
    protected void doOnEvent(ExecutionEvent event) {
        getEventRecorder().endEvent(event);
    }
}