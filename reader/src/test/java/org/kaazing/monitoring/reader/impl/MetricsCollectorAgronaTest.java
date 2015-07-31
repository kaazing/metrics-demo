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
package org.kaazing.monitoring.reader.impl;
import static org.junit.Assert.*;

import java.util.List;
import java.util.function.BiConsumer;

import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;
import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.Metric;

public class MetricsCollectorAgronaTest {
    private static final String COUNTER2 = "counter2";
    private static final String COUNTER1 = "counter1";
    private Mockery context = new JUnit4Mockery() {
    };

    @SuppressWarnings("unchecked")
    @Test
    public void testGetMetricsShouldReturnEmptyList() {
        context.setImposteriser(ClassImposteriser.INSTANCE);
        CountersManagerEx counterManager = context.mock(CountersManagerEx.class);
        context.checking(new Expectations() {{
            oneOf(counterManager).forEach(with(aNonNull(BiConsumer.class)));
        }});
        MetricsCollectorAgrona collector = new MetricsCollectorAgrona(counterManager);
        assertNotNull(collector.getMetrics());
    }

    @Test
    public void getMetricsShouldReturnAvailableMetrics () {
        context.setImposteriser(ClassImposteriser.INSTANCE);
        CountersManagerEx counterManager = context.mock(CountersManagerEx.class);
        context.checking(new Expectations() {{
            oneOf(counterManager).forEach(with(aNonNull(BiConsumer.class)));
            will(new CustomAction("report data") {

                @Override
                public Object invoke(Invocation arg0) throws Throwable {
                    BiConsumer<Integer, String> visitor = (BiConsumer<Integer, String>)arg0.getParameter(0);
                    visitor.accept(0, COUNTER1);
                    visitor.accept(1, COUNTER2);
                    return null;
                }

            });
            oneOf(counterManager).getLongValueForId(0); will(returnValue(24L));
            oneOf(counterManager).getLongValueForId(1); will(returnValue(48L));
        }});
        MetricsCollectorAgrona collector = new MetricsCollectorAgrona(counterManager);
        List<Metric> metrics = collector.getMetrics();
        assertNotNull(metrics);
        assertEquals(COUNTER1, metrics.get(0).getName());
        assertEquals(24L, metrics.get(0).getValue());
        assertEquals(COUNTER2, metrics.get(1).getName());
        assertEquals(48L, metrics.get(1).getValue());
    }

}
