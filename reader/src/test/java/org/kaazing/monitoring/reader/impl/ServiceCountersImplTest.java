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
import org.kaazing.monitoring.reader.api.ServiceCounters;

public class ServiceCountersImplTest {

    private final static String SERVICE_NAME = "test";

    @SuppressWarnings("unchecked")
    @Test
    public void testServiceCounters() {

        Mockery context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);

        CountersManagerEx countersManager = context.mock(CountersManagerEx.class);
        context.checking(new Expectations() {{
            oneOf(countersManager).forEach(with(aNonNull(BiConsumer.class)));
        }});

        ServiceCounters serviceCounters = new ServiceCountersImpl(SERVICE_NAME, countersManager);
        assertEquals(SERVICE_NAME, serviceCounters.getName());
        assertNotNull(serviceCounters.getCounters());
    }
}
