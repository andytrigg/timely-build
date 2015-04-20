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

import org.apache.commons.io.IOUtils;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component(role = ReportGenerator.class)
public class HtmlReportGenerator implements ReportGenerator {

    @Requirement
    private ReportWriterFactory reportWriterFactory;
    @Requirement
    private HtmlFactory  HtmlFactory;

    public void createReportFor(MavenSession mavenSession, List<EventRecorder.TimedEvent> timedEvents) {
        Writer writer = null;
        try {
            writer = getReportWriterFactory().createWriter(mavenSession);
            writer.write(getHtmlFactory().build(timedEvents));
        } catch (IOException e) {
            throw new RuntimeException("Unable to generate report for Timely.", e);

        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    ReportWriterFactory getReportWriterFactory() {
        return reportWriterFactory;
    }

    HtmlFactory getHtmlFactory() {
        return HtmlFactory;
    }
}
