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
package com.sloshydog.timely;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.context.FieldValueResolver;
import org.codehaus.plexus.component.annotations.Component;

import java.io.IOException;
import java.util.List;

@Component(role = HtmlFactory.class)
public class HtmlFactory {

    public String build(List<EventRecorder.TimedEvent> timedEvents) {
        try {
            return new Handlebars().compile("template").apply(Context.newBuilder(timedEvents).resolver(FieldValueResolver.INSTANCE).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
