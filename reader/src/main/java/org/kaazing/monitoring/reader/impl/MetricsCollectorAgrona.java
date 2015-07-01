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
import java.util.ArrayList;
import java.util.List;

import org.kaazing.monitoring.reader.Configuration;
import org.kaazing.monitoring.reader.api.Metric;
import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.kaazing.monitoring.reader.file.location.MonitoringFolderAgrona;
import org.kaazing.monitoring.reader.file.location.impl.MonitoringFolderAgronaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.real_logic.agrona.IoUtil;
import uk.co.real_logic.agrona.concurrent.CountersManager;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class MetricsCollectorAgrona implements MetricsCollector {

    private static int COUNTER_LABELS_BUFFER_LENGTH = 32 * 1024 * 1024;
    private static int COUNTER_VALUES_BUFFER_LENGTH = 1024 * 1024;

    private static int METADATA_BUFFER_LENGTH = 64;

    private CountersManager countersManager;
    private UnsafeBuffer valuesBuffer;
    private UnsafeBuffer labelsBuffer;

    private boolean initialized = false;

    private String fileName;
    private MonitoringFolderAgrona monitoringFolder;
    private File tmpDir;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollectorAgrona.class);

    /**
     * Constructor creating the metrics collector based on config and gateway id
     * @param config
     * @param gatewayId
     */
    public MetricsCollectorAgrona(Configuration config, String gatewayId) {
        this(config.get(Configuration.CFG_AGRONA_MONITORING_FILE) + gatewayId);
    }

    /**
     * Constructor creating the metrics collector based on file name and config
     * @param fileName
     * @param config
     */
    public MetricsCollectorAgrona(String fileName) {
        monitoringFolder = new MonitoringFolderAgronaImpl();
        this.fileName = fileName;
        tmpDir = new File(monitoringFolder.getMonitoringDir(), this.fileName);
    }

    @Override
    public boolean initialize() {

        if (initialized) {
            LOGGER.debug("Resources already initialized. Exiting method.");
            return initialized;
        }

        try {
            MappedByteBuffer mapNewFile = IoUtil.mapExistingFile(tmpDir, fileName);

            labelsBuffer = new UnsafeBuffer(mapNewFile, METADATA_BUFFER_LENGTH, COUNTER_LABELS_BUFFER_LENGTH);
            valuesBuffer =
                    new UnsafeBuffer(mapNewFile, METADATA_BUFFER_LENGTH + COUNTER_LABELS_BUFFER_LENGTH,
                            COUNTER_VALUES_BUFFER_LENGTH);

            countersManager = new CountersManager(labelsBuffer, valuesBuffer);

            initialized = true;
        } catch (Exception e) {
            LOGGER.debug(e.toString());
        }

        return initialized;
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<Metric>();

        if (initialized) {
            countersManager.forEach((id, label) -> {

                label = fileName + "-" + label;
                final int offset = CountersManager.counterOffset(id);
                final long value = valuesBuffer.getLongVolatile(offset);
                LOGGER.debug(String.format("%3d: %,10d - %s", id, value, label));

                metrics.add(new MetricImpl(label, value));
            });
        }

        return metrics;
    }

}
