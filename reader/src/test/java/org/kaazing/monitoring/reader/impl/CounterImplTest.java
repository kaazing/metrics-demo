/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
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

import java.util.function.BiConsumer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.lib.action.CustomAction;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.Counter;

public class CounterImplTest {

    private static final String COUNTER1 = "counter1";

    @SuppressWarnings("unchecked")
    @Test
    public void testCounter() {
        Mockery context = new Mockery();

        context.setImposteriser(ClassImposteriser.INSTANCE);
        CountersManagerEx countersManager = context.mock(CountersManagerEx.class);
        context.checking(new Expectations() {{
            oneOf(countersManager).forEach(with(aNonNull(BiConsumer.class)));
            will(new CustomAction("report data") {

                @Override
                public Object invoke(Invocation arg0) throws Throwable {
                    BiConsumer<Integer, String> visitor = (BiConsumer<Integer, String>)arg0.getParameter(0);
                    visitor.accept(0, COUNTER1);
                    return null;
                }

            });
            oneOf(countersManager).getLongValueForId(0); will(returnValue(24L));
        }});

        Counter counter = new CounterImpl(COUNTER1, 0, countersManager);
        assertEquals(COUNTER1, counter.getLabel());
        assertEquals(24, counter.getValue());
    }

}
