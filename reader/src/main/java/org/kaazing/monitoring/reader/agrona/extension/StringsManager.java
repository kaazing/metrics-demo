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

import java.nio.ByteOrder;
import java.util.function.BiConsumer;

import uk.co.real_logic.agrona.concurrent.AtomicBuffer;

/**
 * Manages the allocation and freeing of String monitoring entities.
 */
public class StringsManager {

    public static final int LABEL_SIZE = 1024;
    public static final int STRING_ENTITY_SIZE = 1024;
    public static final int UNREGISTERED_STRING_SIZE = -1;

    private static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();

    private final AtomicBuffer labelsBuffer;
    private final AtomicBuffer valuesBuffer;

    /**
     * Create a new String monitoring entity buffer manager over two buffers.
     *
     * @param labelsBuffer containing the human readable labels for the monitoring entities.
     * @param metricsBuffer containing the values of the String monitoring entities themselves.
     */
    public StringsManager(final AtomicBuffer labelsBuffer, final AtomicBuffer valuesBuffer) {
        this.labelsBuffer = labelsBuffer;
        this.valuesBuffer = valuesBuffer;
        valuesBuffer.verifyAlignment();
    }

    /**
     * Iterate over all labels in the label buffer.
     *
     * @param consumer function to be called for each label.
     */
    public void forEach(final BiConsumer<Integer, String> consumer) {
        int labelsOffset = 0;
        int size;
        int id = 0;

        while ((size = labelsBuffer.getInt(labelsOffset)) != 0) {
            if (size != UNREGISTERED_STRING_SIZE) {
                final String label = labelsBuffer.getStringUtf8(labelsOffset, NATIVE_BYTE_ORDER);
                consumer.accept(id, label);
            }

            labelsOffset += LABEL_SIZE;
            id++;
        }
    }

    /**
     * Retrieves the value for the atomic string with the given id.
     *
     * @param id for which the value should be provided.
     * @param byteOrder for the string.
     * @return the value of the atomic string.
     */
    public String getStringValueUTF8(int id, ByteOrder byteOrder) {
        int offset = getEntityOffset(id);

        return valuesBuffer.getStringUtf8(offset, byteOrder);
    }

    /**
     * The offset in the values buffer for a given id.
     *
     * @param id for which the offset should be provided.
     * @return the offset in the values buffer.
     */
    public static int getEntityOffset(int id) {
        return id * STRING_ENTITY_SIZE;
    }

}
