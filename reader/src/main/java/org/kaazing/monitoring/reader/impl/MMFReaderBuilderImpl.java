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
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.GatewayCounters;
import org.kaazing.monitoring.reader.api.MMFReader;
import org.kaazing.monitoring.reader.api.MMFReaderBuilder;
import org.kaazing.monitoring.reader.api.ServiceCounters;

import uk.co.real_logic.agrona.BitUtil;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class MMFReaderBuilderImpl implements MMFReaderBuilder {

    private static final int SIZE_OF_INT = BitUtil.SIZE_OF_INT;
    private static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();

    private MappedByteBuffer mappedByteBuffer;

    private UnsafeBuffer metaDataBuffer;

    private int fileVersion;
    private String gatewayId;
    private int gatewayDataOffset;
    private int serviceMappingsOffset;

    public MMFReaderBuilderImpl(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    @Override
    public MMFReader build() {
        readMetadata(0);

        CountersManagerEx countersManager = buildCountersManager(gatewayDataOffset + gatewayId.length() + SIZE_OF_INT);
        GatewayCounters gateway = new GatewayCountersImpl(gatewayId, countersManager);
        List<ServiceCounters> services = getServices(serviceMappingsOffset);

        MMFReaderImpl reader = new MMFReaderImpl(fileVersion, gateway, services);
        return reader;
    }

    /**
     * Reads the metadata information
     * @param offset - offset from which to read the metadata
     */
    private void readMetadata(int offset) {
        metaDataBuffer = new UnsafeBuffer(mappedByteBuffer, offset, mappedByteBuffer.capacity());

        fileVersion = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;
        gatewayDataOffset = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;
        serviceMappingsOffset = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;

        gatewayId = metaDataBuffer.getStringUtf8(gatewayDataOffset, NATIVE_BYTE_ORDER);
    }

    /**
     * Reads the offsets for the labels and values buffers and creates a countersManager
     * @param offset - offset from which to start reading the buffers' offsets
     * @return CountersManagerEx
     */
    private CountersManagerEx buildCountersManager(int offset) {
        int labelsBufferOffset = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;
        int labelsBufferLength = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;
        int valuesBufferOffset = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;
        int valuesBufferLength = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;

        if (labelsBufferLength != 0 && valuesBufferLength != 0) {
            return createCountersManager(labelsBufferOffset, labelsBufferLength, valuesBufferOffset, valuesBufferLength);
        }
        return null;
    }

    /**
     * Reads the service offset data and returns a list with all the services
     * @param offset - offset from which to start reading the buffers' offsets
     * @return List<ServiceCounters>
     */
    private List<ServiceCounters> getServices(int offset) {
        List<ServiceCounters> services = new ArrayList<ServiceCounters>();

        int numberOfServices = metaDataBuffer.getInt(offset);
        offset += SIZE_OF_INT;

        for (int i = 0; i < numberOfServices; i++) {
            CountersManagerEx countersManager;
            ServiceCounters service;

            String name = metaDataBuffer.getStringUtf8(offset, NATIVE_BYTE_ORDER);
            offset += name.length() + SIZE_OF_INT;

            int serviceOffset = metaDataBuffer.getInt(offset);
            offset += SIZE_OF_INT;

            countersManager = buildCountersManager(serviceOffset);

            service = new ServiceCountersImpl(name, countersManager);
            services.add(service);
        }

        return services;
    }

    /**
     * Instantiates a labels buffer and a values buffer and creates a countersManager
     * @param labelsBufferOffset - starting offset for the labels buffer
     * @param labelsBufferLength - length of the labels buffer
     * @param valuesBufferOffset - starting offset for the values buffer
     * @param valuesBufferLength - length of the values buffer
     * @return
     */
    private CountersManagerEx createCountersManager(int labelsBufferOffset, int labelsBufferLength, int valuesBufferOffset,
        int valuesBufferLength) {
        UnsafeBuffer labelsBuffer = new UnsafeBuffer(mappedByteBuffer, labelsBufferOffset, labelsBufferLength);
        UnsafeBuffer valuesBuffer = new UnsafeBuffer(mappedByteBuffer, valuesBufferOffset, valuesBufferLength);

        return new CountersManagerEx(labelsBuffer, valuesBuffer);
    }
}
