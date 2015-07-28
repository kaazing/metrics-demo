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

package org.kaazing.monitoring.reader;

import java.nio.MappedByteBuffer;

import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;

import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

/**
 * Factory class for creating managers for Agrona management
 */
public class AgronaManagementFactory {
    private static int COUNTER_LABELS_BUFFER_LENGTH = 32 * 1024 * 1024;
    private static int COUNTER_VALUES_BUFFER_LENGTH = 1024 * 1024;

    private static int METADATA_BUFFER_LENGTH = 64;

    MappedByteBuffer mappedByteBuffer;

    private CountersManagerEx countersManager;

    public AgronaManagementFactory(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    public CountersManagerEx createCountersManager() {
        UnsafeBuffer counterLabelsBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH, COUNTER_LABELS_BUFFER_LENGTH);
        UnsafeBuffer counterValuesBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH + COUNTER_LABELS_BUFFER_LENGTH,
                        COUNTER_VALUES_BUFFER_LENGTH);

        countersManager = new CountersManagerEx(counterLabelsBuffer, counterValuesBuffer);
        return countersManager;
    }

}
