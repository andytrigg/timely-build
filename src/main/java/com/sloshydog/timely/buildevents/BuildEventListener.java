/**
 * Copyright (C) 2013 david@gageot.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.sloshydog.timely.buildevents;

import com.github.jknack.handlebars.*;
import com.github.jknack.handlebars.context.*;
import org.apache.maven.execution.AbstractExecutionListener;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BuildEventListener extends AbstractExecutionListener {

    private final File output;
    private final MavenSession mavenSession;
    private final Map<String, Long> startTimes = new ConcurrentHashMap<String, Long>();
    private final Map<String, Long> endTimes = new ConcurrentHashMap<String, Long>();

    public BuildEventListener(File output, MavenSession mavenSession) {
        this.output = output;
        this.mavenSession = mavenSession;
    }

    @Override
    public void mojoStarted(ExecutionEvent event) {
        startTimes.put(key(event), System.currentTimeMillis());
    }

    @Override
    public void mojoSkipped(ExecutionEvent event) {
        mojoEnd(event);
    }

    @Override
    public void mojoSucceeded(ExecutionEvent event) {
        mojoEnd(event);
    }

    @Override
    public void mojoFailed(ExecutionEvent event) {
        mojoEnd(event);
    }

    private void mojoEnd(ExecutionEvent event) {
        endTimes.put(key(event), System.currentTimeMillis());
    }

    @Override
    public void sessionEnded(ExecutionEvent event) {
        report();
    }

    private String key(ExecutionEvent event) {
        MojoExecution mojo = event.getMojoExecution();
        String goal = mojo.getGoal();
        String phase = mojo.getLifecyclePhase();
        String group = event.getProject().getGroupId();
        String project = event.getProject().getArtifactId();
        return group + "/" + project + "/" + phase + "/" + goal;
    }

    public void report() {
        long buildStartTime = Collections.min(startTimes.values());

        List<Measure> measures = new ArrayList<Measure>();
        for (String key : startTimes.keySet()) {
            String[] keyParts = key.split("/");

            Measure measure = new Measure();
            measure.group = keyParts[0];
            measure.project = keyParts[1];
            measure.phase = keyParts[2];
            measure.goal = keyParts[3];
            measure.start = startTimes.get(key) - buildStartTime;
            measure.end = endTimes.get(key) - buildStartTime;
            measures.add(measure);
        }

        Collections.sort(measures);

        try {
            String html = new Handlebars().compile("template").apply(Context.newBuilder(measures).resolver(FieldValueResolver.INSTANCE).build());
            write(html);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(String message) throws IOException {
        File path = output.getParentFile();
        if (!path.exists()) {
            if (!path.mkdirs()) {
                throw new IOException("Unable to create " + path);
            }
        }

        FileWriter writer = new FileWriter(output);
        writer.write(message);
        writer.close();
    }

    public static class ReportContext {
        MavenSession mavenSession;
        List<Measure> measures;

        public ReportContext(MavenSession mavenSession, List<Measure> measures) {

            this.mavenSession = mavenSession;
            this.measures = measures;
        }
    }
    public static class Measure implements Comparable<Measure> {

        String group;
        String project;
        String phase;
        String goal;
        Long start;
        Long end;

        @Override
        public int compareTo(Measure other) {
            return start.compareTo(other.start);
        }
    }
}