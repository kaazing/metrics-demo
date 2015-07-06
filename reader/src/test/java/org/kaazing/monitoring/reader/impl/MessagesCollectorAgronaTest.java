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

import static org.junit.Assert.assertNotNull;

import java.util.function.BiConsumer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.kaazing.monitoring.reader.agrona.extension.StringsManager;

public class MessagesCollectorAgronaTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testMessagesCollector() {
        Mockery context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);
        StringsManager stringsManager = context.mock(StringsManager.class);
        context.checking(new Expectations() {{
            oneOf(stringsManager).forEach(with(aNonNull(BiConsumer.class)));
        }});
        MessagesCollectorAgrona collector = new MessagesCollectorAgrona(stringsManager);
        assertNotNull(collector.getMessages());
    }

}
