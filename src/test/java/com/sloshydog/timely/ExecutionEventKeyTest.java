package com.sloshydog.timely;

import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExecutionEventKeyTest {
    @Mock
    private ExecutionEvent event;
    @Mock
    private MavenProject mavenProject;
    @Mock
    private MojoExecution mojoExecution;

    @Before
    public void setUp() {
        when(event.getProject()).thenReturn(mavenProject);
        when(event.getMojoExecution()).thenReturn(mojoExecution);
    }

    @Test
    public void shouldBeAbleToGetPhaseFromExecutionEventKey() {
        when(mojoExecution.getLifecyclePhase()).thenReturn("phase");

        assertThat(new ExecutionEventKey(event).getPhase()).isEqualTo("phase");
    }

    @Test
    public void shouldBeAbleToGetGoalFromExecutionEventKey() {
        when(mojoExecution.getGoal()).thenReturn("goal");

        assertThat(new ExecutionEventKey(event).getGoal()).isEqualTo("goal");
    }

    @Test
    public void shouldBeAbleToGetGroupFromExecutionEventKey() {
        when(mavenProject.getGroupId()).thenReturn("group");

        assertThat(new ExecutionEventKey(event).getGroup()).isEqualTo("group");
    }

    @Test
    public void shouldBeAbleToGetProjectFromExecutionEventKey() {
        when(mavenProject.getArtifactId()).thenReturn("project");

        assertThat(new ExecutionEventKey(event).getProject()).isEqualTo("project");
    }

}