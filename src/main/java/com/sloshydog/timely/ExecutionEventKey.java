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
import org.apache.maven.plugin.MojoExecution;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class ExecutionEventKey {
    private final String group;
    private final String project;
    private final String phase;
    private final String goal;

    public ExecutionEventKey(ExecutionEvent event) {
        group = event.getProject().getGroupId();
        project = event.getProject().getArtifactId();
        MojoExecution mojo = event.getMojoExecution();
        phase = mojo.getLifecyclePhase();
        goal = mojo.getGoal();
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }
}
