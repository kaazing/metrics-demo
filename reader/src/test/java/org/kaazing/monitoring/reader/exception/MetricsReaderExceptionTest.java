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
package org.kaazing.monitoring.reader.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetricsReaderExceptionTest {
    private static final String MESSAGE = "test";

    @Test
    public void testMetricsReaderExceptionMessage() {
        MetricsReaderException exc = new MetricsReaderException(MESSAGE);
        assertEquals(MESSAGE, exc.getMessage());
    }

    @Test
    public void testMetricsReaderExceptionMessageThrowable() {
        MetricsReaderException exc = new MetricsReaderException(MESSAGE, new Throwable(MESSAGE));
        assertEquals(MESSAGE, exc.getMessage());
        exc = new MetricsReaderException(MESSAGE, new Throwable(exc));
        assertEquals(MESSAGE, exc.getMessage());
    }

    @Test
    public void testMetricsReaderExceptionThrowable() {
        MetricsReaderException exc = new MetricsReaderException(new Throwable(MESSAGE));
        assertEquals("java.lang.Throwable: " + MESSAGE, exc.getMessage());
    }

}
