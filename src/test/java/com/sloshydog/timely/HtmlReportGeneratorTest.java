package com.sloshydog.timely;

import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2014. gigantiqandy@gmail.com
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(MockitoJUnitRunner.class)
public class HtmlReportGeneratorTest {
    @Test
    public void shouldBeAReportGenerator() {
        assertThat(ReportGenerator.class).isAssignableFrom(HtmlReportGenerator.class);
    }

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(HtmlReportGenerator.class).hasAnnotation(Component.class);
    }

    @Test
    public void shouldWriteOutHtmlReportForTheTimedEvents() throws IOException {
        MavenSession mavenSession = mock(MavenSession.class);
        List<EventRecorder.TimedEvent> timedEvents = Collections.emptyList();

        HtmlReportGenerator reportGeneratorSpy = spy(new HtmlReportGenerator());
        ReportWriterFactory reportWriterFactory = mock(ReportWriterFactory.class);
        doReturn(reportWriterFactory).when(reportGeneratorSpy).getReportWriterFactory();
        HtmlFactory htmlFactory = mock(HtmlFactory.class);
        doReturn(htmlFactory).when(reportGeneratorSpy).getHtmlFactory();
        when(htmlFactory.build(timedEvents)).thenReturn("html output");
        Writer writer = mock(Writer.class);
        when(reportWriterFactory.createWriter(mavenSession)).thenReturn(writer);

        reportGeneratorSpy.createReportFor(mavenSession, timedEvents);

        InOrder inOrderVerifier = Mockito.inOrder(writer);
        inOrderVerifier.verify(writer).write("html output");
        inOrderVerifier.verify(writer).close();
    }

    @Test
    public void shouldEnsureThatTheWriterIsClosedOnError() throws IOException {
        MavenSession mavenSession = mock(MavenSession.class);
        List<EventRecorder.TimedEvent> timedEvents = Collections.emptyList();

        HtmlReportGenerator reportGeneratorSpy = spy(new HtmlReportGenerator());
        ReportWriterFactory reportWriterFactory = mock(ReportWriterFactory.class);
        doReturn(reportWriterFactory).when(reportGeneratorSpy).getReportWriterFactory();
        doThrow(new RuntimeException()).when(reportGeneratorSpy).getHtmlFactory();

        Writer writer = mock(Writer.class);
        when(reportWriterFactory.createWriter(mavenSession)).thenReturn(writer);

        try {
            reportGeneratorSpy.createReportFor(mavenSession, timedEvents);
            fail("Should have thrown exception");
        } catch (Exception e) {
            verify(writer).close();
        }

    }

}