package com.sloshydog.timely;

import com.sloshydog.timely.buildevents.BuildEventListener;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.util.Date;

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
@Component(role = ExecutionListenerFactory.class)
public class TimelyBuildExecutionListenerFactory implements ExecutionListenerFactory {
    private static final String DEFAULT_FILE_DESTINATION = "target/timeline-%d.html";

    @Override
    public ExecutionListener create(MavenSession mavenSession) {
        return new BuildEventListener(logFile(mavenSession), mavenSession);
    }

    private File logFile(MavenSession session) {
        String path = String.format(DEFAULT_FILE_DESTINATION, new Date().getTime());
        String buildDir = session.getExecutionRootDirectory();
        return new File(buildDir, path);
    }
}
