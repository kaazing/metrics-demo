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
package org.kaazing.monitoring.reader.agrona.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;

import uk.co.real_logic.agrona.concurrent.AtomicBuffer;

public class CountersManagerExTest {

    @Test
    public void getLongValueForIdShouldReturnCounterValue() {

        Mockery context = new Mockery();

        context.setImposteriser(ClassImposteriser.INSTANCE);

        AtomicBuffer buffer = context.mock(AtomicBuffer.class);
        context.checking(new Expectations() {{
            oneOf(buffer).verifyAlignment();
            oneOf(buffer).capacity();
            oneOf(buffer).getLongVolatile(0);will(returnValue(new Long(0)));
        }});

        CountersManagerEx manager = new CountersManagerEx(buffer, buffer);
        assertNotNull(manager);

        assertEquals(manager.getLongValueForId(0).intValue(), 0);
    }
}
