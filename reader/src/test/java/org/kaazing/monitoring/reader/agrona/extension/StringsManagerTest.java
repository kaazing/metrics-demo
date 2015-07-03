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
package org.kaazing.monitoring.reader.agrona.extension;

import static org.junit.Assert.assertFalse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import uk.co.real_logic.agrona.concurrent.AtomicBuffer;

public class StringsManagerTest {
    private Mockery context = new JUnit4Mockery() {
    };

    @Test
    public void testStringsManager() {
        AtomicBuffer buffer = context.mock(AtomicBuffer.class);
        context.checking(new Expectations() {{
            oneOf(buffer).verifyAlignment();
        }});
        StringsManager manager = new StringsManager(buffer, buffer);
        assertFalse(manager.equals(null));
    }

}