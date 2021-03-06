/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.monitoring.reader.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.function.BiConsumer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.GatewayCounters;

public class GatewayCountersImplTest {

    private final static String GATEWAY_ID = "testId";

    @SuppressWarnings("unchecked")
    @Test
    public void testGatewayCounters() {

        Mockery context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);

        CountersManagerEx countersManager = context.mock(CountersManagerEx.class);
        context.checking(new Expectations() {{
            oneOf(countersManager).forEach(with(aNonNull(BiConsumer.class)));
        }});

        GatewayCounters gatewayCounters = new GatewayCountersImpl(GATEWAY_ID, countersManager);
        assertEquals(GATEWAY_ID, gatewayCounters.getGatewayId());
        assertNotNull(gatewayCounters.getCounters());
    }
}
