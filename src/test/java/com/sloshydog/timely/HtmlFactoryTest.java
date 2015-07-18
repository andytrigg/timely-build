package com.sloshydog.timely;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
public class HtmlFactoryTest {

    public static final String EXPECTED_HTML_RESULT = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "\n" +
            "<script type=\"text/javascript\" src=\"https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization',\n" +
            "       'version':'1','packages':['timeline']}]}\"></script>\n" +
            "<script type=\"text/javascript\">\n" +
            "google.setOnLoadCallback(drawChart);\n" +
            "\n" +
            "function drawChart() {\n" +
            "  var container = document.getElementById('example1');\n" +
            "\n" +
            "  var chart = new google.visualization.Timeline(container);\n" +
            "\n" +
            "  var dataTable = new google.visualization.DataTable();\n" +
            "\n" +
            "  dataTable.addColumn({ type: 'string', id: 'Project' });\n" +
            "  dataTable.addColumn({ type: 'string', id: 'Phase/Goal' });\n" +
            "  dataTable.addColumn({ type: 'date', id: 'Start' });\n" +
            "  dataTable.addColumn({ type: 'date', id: 'End' });\n" +
            "\n" +
            "  dataTable.addRows([\n" +
            "\n" +
            "    ['artifact id', 'phase:phase goal:goal', new Date(0), new Date(1000) ],\n" +
            "  ]);\n" +
            "\n" +
            "  var options = {\n" +
            "    timeline: { groupByRowLabel: true }\n" +
            "  };\n" +
            "\n" +
            "  chart.draw(dataTable, options);\n" +
            "}\n" +
            "</script>\n" +
            "<div id=\"example1\" style=\"width: 1900px; height: 1200px;\"></div>\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void shouldBeAnnotatedAsAComponent() {
        assertThat(HtmlFactory.class).hasAnnotation(Component.class);
    }

    @Test
    public void should() {
        ExecutionEvent event = mock(ExecutionEvent.class);

        MavenProject mavenProject = mock(MavenProject.class);
        when(event.getProject()).thenReturn(mavenProject);
        when(mavenProject.getGroupId()).thenReturn("group id");
        when(mavenProject.getArtifactId()).thenReturn("artifact id");
        MojoExecution mojoExecution = mock(MojoExecution.class);
        when(event.getMojoExecution()).thenReturn(mojoExecution);
        when(mojoExecution.getLifecyclePhase()).thenReturn("phase");
        when(mojoExecution.getGoal()).thenReturn("goal");

        EventRecorder.TimedEvent timedEvent = new EventRecorder.TimedEvent(new ExecutionEventKey(event), 0l, 1000l);
        List<EventRecorder.TimedEvent> timedEvents = Collections.singletonList(timedEvent);
        assertThat(new HtmlFactory().build(timedEvents)).isEqualTo(EXPECTED_HTML_RESULT);
    }
}
