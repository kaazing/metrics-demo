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

import java.io.File;
import java.nio.MappedByteBuffer;
import java.nio.file.Paths;

import org.kaazing.monitoring.reader.api.MMFReader;
import org.kaazing.monitoring.reader.api.MMFReaderBuilder;
import org.kaazing.monitoring.reader.api.MonitoringDataProcessor;
import org.kaazing.monitoring.reader.exception.MetricsReaderException;
import org.kaazing.monitoring.reader.file.location.MonitoringFolderAgrona;
import org.kaazing.monitoring.reader.impl.file.location.MonitoringFolderAgronaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.real_logic.agrona.IoUtil;

public class AgronaMonitoringDataProcessor implements MonitoringDataProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgronaMonitoringDataProcessor.class);

    private boolean initialized;

    private MappedByteBuffer mappedFile;
    private MMFReaderBuilder readerBuilder;
    private String fileName;

    public AgronaMonitoringDataProcessor(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean initialize() throws MetricsReaderException {

        if (initialized) {
            LOGGER.debug("Resources already initialized. Exiting method.");
            return initialized;
        }

        try {
            mappedFile = getMappedFile();
            readerBuilder = new MMFReaderBuilderImpl(mappedFile);
            initialized = true;
        } catch (IllegalStateException e) {
            LOGGER.error(e.toString());
        }

        return initialized;
    }

    @Override
    public MMFReader getMMFReader() {
        if (initialized) {
            return readerBuilder.build();
        }
        return null;
    }

    /**
     * Initializes and maps the Agrona monitoring file
     * @return MappedByteBuffer - the Agrona mapped file
     */
    private MappedByteBuffer getMappedFile() {
        File file = new File(fileName);
        
        if (!file.isAbsolute()) { 
            MonitoringFolderAgrona agronaFolder = new MonitoringFolderAgronaImpl();
            file = new File(agronaFolder.getMonitoringDir(), fileName);
        }

        return IoUtil.mapExistingFile(file, fileName);
    }
}
