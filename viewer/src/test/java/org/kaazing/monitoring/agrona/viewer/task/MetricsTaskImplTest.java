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
package org.kaazing.monitoring.agrona.viewer.task;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.kaazing.monitoring.reader.api.MetricsCollector;

public class MetricsTaskImplTest {

    private static final String FILE_NAME = "test";
    private Mockery context = new JUnit4Mockery() {
    };

    @Test
    public void testGetFileName() {
        ScheduledExecutorService taskExecutor = context.mock(ScheduledExecutorService.class);
        MetricsCollector metricsCollector = context.mock(MetricsCollector.class);
        getScheduledTask(taskExecutor);
        MetricsTask task = new MetricsTaskImpl(FILE_NAME, taskExecutor, metricsCollector);
        assertEquals(FILE_NAME, task.getFileName());
    }

    @Test
    public void testGetMetricsCollector() {
        ScheduledExecutorService taskExecutor = context.mock(ScheduledExecutorService.class);
        MetricsCollector metricsCollector = context.mock(MetricsCollector.class);
        getScheduledTask(taskExecutor);
        MetricsTask task = new MetricsTaskImpl(FILE_NAME, taskExecutor, metricsCollector);
        assertEquals(metricsCollector, task.getMetricsCollector());
    }

    @Test
    public void testGetScheduledTask() {
        ScheduledExecutorService taskExecutor = context.mock(ScheduledExecutorService.class);
        MetricsCollector metricsCollector = context.mock(MetricsCollector.class);
        ScheduledFuture<?> sched = getScheduledTask(taskExecutor);
        MetricsTask task = new MetricsTaskImpl(FILE_NAME, taskExecutor, metricsCollector);
        assertEquals(sched, task.getScheduledTask());
    }

    /**
     * Method returning scheduled task
     * @param taskExecutor
     * @return
     */
    private ScheduledFuture<?> getScheduledTask(ScheduledExecutorService taskExecutor) {
        ScheduledFuture<?> sched = null;
        context.checking(new Expectations() {{
            oneOf(taskExecutor).scheduleAtFixedRate(with(any(Runnable.class)), with(any(Long.class)),  with(any(Long.class)),  with(any(TimeUnit.class)));
            will(returnValue(sched));
        }});
        return sched;
    }
}