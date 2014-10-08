package com.sloshydog.timely;

import com.sloshydog.timely.buildevents.BuildEventListener;
import com.sloshydog.timely.buildevents.ExecutionListenerChain;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.util.Date;

@Component(role = AbstractMavenLifecycleParticipant.class, hint = "buildevents")
public class TimelyBuildEventsExtension extends AbstractMavenLifecycleParticipant {
  private static final String DEFAULT_FILE_DESTINATION = "target/timeline-%d.html";

  @Override
  public void afterProjectsRead(MavenSession session) {
    MavenExecutionRequest request = session.getRequest();

    ExecutionListener original = request.getExecutionListener();
    BuildEventListener listener = new BuildEventListener(logFile(session));
    ExecutionListener chain = new ExecutionListenerChain(original, listener);

    request.setExecutionListener(chain);
  }

  private File logFile(MavenSession session) {
      String path = String.format(DEFAULT_FILE_DESTINATION, new Date().getTime());
      String buildDir = session.getExecutionRootDirectory();
      return new File(buildDir, path);
  }
}

