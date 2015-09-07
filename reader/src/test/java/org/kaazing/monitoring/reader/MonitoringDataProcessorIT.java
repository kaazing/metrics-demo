/**
 * Copyright (c) 2007-2014 Kaazing Corporation. All rights reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kaazing.monitoring.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.RuleChain.outerRule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.Metrics;
import org.kaazing.monitoring.reader.api.MetricsFileProcessor;
import org.kaazing.monitoring.reader.api.ServiceCounters;
import org.kaazing.monitoring.reader.impl.MetricsFileProcessorImpl;
import org.kaazing.monitoring.test.WriteToFileRule;

/**
 * For an example of using a Socket to interact with a running robot script
 * see test https://github.com/kaazing/enterprise.gateway/blob/develop/service/jms/src/test/java/com/kaazing/gateway/jms/server/service/jms/cache/LvcTopicConnectionLifetimeIT.java
 * and utility class https://github.com/kaazing/enterprise.gateway/blob/develop/service/jms/src/test/java/com/kaazing/gateway/jms/server/test/ScriptPilot.java
 * which the test uses. *
 * To learn about k3po (robot) see the README.md and wiki pages in https://github.com/k3po/k3po,
 * especially https://github.com/k3po/k3po/wiki/Scripting-Language
 */
public class MonitoringDataProcessorIT {
    private TestRule timeout = new DisableOnDebug(new Timeout(10, TimeUnit.SECONDS));

    private K3poRule robot = new K3poRule();

    private WriteToFileRule writer = new WriteToFileRule(8444);

    @Rule
    public TestRule chain = outerRule(robot).around(writer).around(timeout);

    @Specification("MMFWithNoServices")
    @Test
    public void shouldReadFileWithNoServices() throws Exception {
        robot.finish();
        Path metricsFile = writer.getOutputFile();
        assertTrue(String.format("Metrics file %s should be found", metricsFile), Files.exists(metricsFile));

        MetricsFileProcessor monitoringDataProcessor = new MetricsFileProcessorImpl(metricsFile.toString());
        monitoringDataProcessor.initialize();
        Metrics reader = monitoringDataProcessor.getMetrics();
        assertNotNull(reader);
        assertEquals("gwy1", reader.getGateway().getGatewayId());
        assertEquals(0, reader.getGateway().getCounters().size());
        assertEquals(0, reader.getServices().size());
    }

    @Specification("MMFWithServices")
    @Test
    public void shouldReadFileWithServices() throws Exception {
        robot.finish();
        Path metricsFile = writer.getOutputFile();
        assertTrue(String.format("Metrics file %s should be found", metricsFile), Files.exists(metricsFile));

        MetricsFileProcessor monitoringDataProcessor = new MetricsFileProcessorImpl(metricsFile.toString());
        monitoringDataProcessor.initialize();
        Metrics reader = monitoringDataProcessor.getMetrics();
        assertNotNull(reader);
        assertEquals("gwy1", reader.getGateway().getGatewayId());
        assertEquals(0, reader.getGateway().getCounters().size());
        assertEquals(2, reader.getServices().size());

        ServiceCounters firstService = reader.getServices().get(0);
        assertEquals(3, firstService.getCounters().size());

        List<Counter> counters = firstService.getCounters();
        assertEquals(1, counters.get(0).getValue());
        assertEquals(2, counters.get(1).getValue());
        assertEquals(3, counters.get(2).getValue());
    }

}
