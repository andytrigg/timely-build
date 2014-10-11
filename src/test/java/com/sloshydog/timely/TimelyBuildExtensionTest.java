/*
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

import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimelyBuildExtensionTest {
    @Mock
    private ExecutionListenerFactory executionListenerFactory;

    @Test
    public void shouldBeAnAbstractMavenLifecycleParticipant() {
        assertThat(AbstractMavenLifecycleParticipant.class).isAssignableFrom(TimelyBuildExtension.class);
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(TimelyBuildExtension.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldHaveAExecutionListenerFactoryFieldAnnotatedAsRequired() throws NoSuchFieldException {
        assertThat(TimelyBuildExtension.class.getDeclaredField("executionListenerFactory").getAnnotation(Requirement.class)).isNotNull();
    }

    @Test
    public void shouldConfigureTheExecutionListenerAfterProjectsRead() throws NoSuchFieldException, IllegalAccessException {
        MavenSession mavenSession = mock(MavenSession.class);
        MavenExecutionRequest request = mock(MavenExecutionRequest.class);
        ExecutionListener executionListener = mock(ExecutionListener.class);

        when(mavenSession.getRequest()).thenReturn(request);
        when(executionListenerFactory.create(mavenSession)).thenReturn(executionListener);

        TimelyBuildExtension buildExtension = new TimelyBuildExtension();
        writeDeclaredField(buildExtension, "executionListenerFactory", executionListenerFactory, true);

        buildExtension.afterProjectsRead(mavenSession);

        verify(request).setExecutionListener(executionListener);
    }

}