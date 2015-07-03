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

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import org.kaazing.monitoring.reader.agrona.extension.StringsManager;
import org.kaazing.monitoring.reader.api.Message;
import org.kaazing.monitoring.reader.api.MessagesCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagesCollectorAgrona implements MessagesCollector {

    private StringsManager stringsManager;

    private static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesCollectorAgrona.class);

    public MessagesCollectorAgrona(StringsManager stringsManager) {
        this.stringsManager = stringsManager;
    }

    @Override
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<Message>();

        stringsManager.forEach((id, label) -> {

            final String value = stringsManager.getStringValueUTF8(id, NATIVE_BYTE_ORDER);
            LOGGER.debug(String.format("%3d: %s - %s", id, value, label));

            messages.add(new MessageImpl(label, value));
        });

        return messages;
    }

}
