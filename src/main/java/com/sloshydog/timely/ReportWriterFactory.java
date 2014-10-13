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

import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Component(role = ReportWriterFactory.class)
public class ReportWriterFactory {

    private static final String OUTPUT_DIR = "timelybuild.output.directory";
    private static final String DEFAULT_DIR = "target";
    private static final String FILENAME = "timelybuild-%d.html";

    public Writer createWriter(MavenSession mavenSession) throws IOException {

        File reportFile = FileUtils.getFile(mavenSession.getUserProperties().getProperty(OUTPUT_DIR, DEFAULT_DIR), String.format(FILENAME, System.currentTimeMillis()));
        File path = reportFile.getParentFile();
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw new IOException("Unable to create " + path);
            }
        }
        return new FileWriter(reportFile);
    }

}
