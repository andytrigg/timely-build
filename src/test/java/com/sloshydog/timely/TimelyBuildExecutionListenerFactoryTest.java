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