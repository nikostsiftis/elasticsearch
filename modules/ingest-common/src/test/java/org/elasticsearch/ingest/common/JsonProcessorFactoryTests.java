/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest.common;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.ingest.TestTemplateService;
import org.elasticsearch.test.ESTestCase;
import org.junit.Before;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

public class JsonProcessorFactoryTests extends ESTestCase {

    private static final JsonProcessor.Factory FACTORY = new JsonProcessor.Factory();

    public void testCreate() throws Exception {
        String processorTag = randomAsciiOfLength(10);
        String randomField = randomAsciiOfLength(10);
        String randomTargetField = randomAsciiOfLength(5);
        Map<String, Object> config = new HashMap<>();
        config.put("field", randomField);
        config.put("target_field", randomTargetField);
        JsonProcessor jsonProcessor = FACTORY.create(null, processorTag, config);
        assertThat(jsonProcessor.getTag(), equalTo(processorTag));
        assertThat(jsonProcessor.getField(), equalTo(randomField));
        assertThat(jsonProcessor.getTargetField(), equalTo(randomTargetField));
    }

    public void testCreateWithDefaultTarget() throws Exception {
        String processorTag = randomAsciiOfLength(10);
        String randomField = randomAsciiOfLength(10);
        Map<String, Object> config = new HashMap<>();
        config.put("field", randomField);
        JsonProcessor jsonProcessor = FACTORY.create(null, processorTag, config);
        assertThat(jsonProcessor.getTag(), equalTo(processorTag));
        assertThat(jsonProcessor.getField(), equalTo(randomField));
        assertThat(jsonProcessor.getTargetField(), equalTo(randomField));
    }

    public void testCreateWithMissingField() throws Exception {
        Map<String, Object> config = new HashMap<>();
        String processorTag = randomAsciiOfLength(10);
        ElasticsearchException exception = expectThrows(ElasticsearchParseException.class,
            () -> FACTORY.create(null, processorTag, config));
        assertThat(exception.getMessage(), equalTo("[field] required property is missing"));
    }
}
