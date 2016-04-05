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
package org.kaazing.monitoring.reader.impl;

import java.io.File;
import java.nio.MappedByteBuffer;

import org.kaazing.monitoring.reader.api.Metrics;
import org.kaazing.monitoring.reader.api.MetricsFileProcessor;
import org.kaazing.monitoring.reader.exception.MetricsReaderException;
import org.kaazing.monitoring.reader.file.location.MonitoringFolder;
import org.kaazing.monitoring.reader.impl.file.location.MonitoringFolderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.real_logic.agrona.IoUtil;

public class MetricsFileProcessorImpl implements MetricsFileProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsFileProcessorImpl.class);

    private boolean initialized;

    private MappedByteBuffer mappedFile;
    private String fileName;

    public MetricsFileProcessorImpl(String fileName) {
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
            initialized = true;
        } catch (IllegalStateException e) {
            LOGGER.error(e.toString());
        }

        return initialized;
    }

    @Override
    public Metrics getMetrics() {
        if (initialized) {
            return Metrics.wrap(mappedFile);
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
            MonitoringFolder agronaFolder = new MonitoringFolderImpl();
            file = new File(agronaFolder.getMonitoringDir(), fileName);
        }

        return IoUtil.mapExistingFile(file, fileName);
    }
}
