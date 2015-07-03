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

import org.kaazing.monitoring.reader.api.MessagesCollector;
import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.kaazing.monitoring.reader.impl.AgronaManagementFactory;
import org.kaazing.monitoring.reader.impl.CountersManagerEx;
import org.kaazing.monitoring.reader.impl.MetricsReaderException;
import org.kaazing.monitoring.reader.impl.MessagesCollectorAgrona;
import org.kaazing.monitoring.reader.impl.MetricsCollectorAgrona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class that creates MetricsCollector or MessagesCollector instance
 */
public class CollectorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectorFactory.class);

    private boolean initialized = false;

    private MappedByteBuffer mappedFile;
    private AgronaManagementFactory agronaManagementFactory;
    private String fileName;

    public CollectorFactory(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Initializes the Agrona monitoring file 
     * @return boolean - returns true if the initialization was finished without any problem
     * @throws MetricsReaderException
     */
    public boolean initialize() throws MetricsReaderException {

        if (initialized) {
            LOGGER.debug("Resources already initialized. Exiting method.");
            return initialized;
        }

        Configuration config = new Configuration();

        if (!config.loadConfigFile()) {
            throw new MetricsReaderException("Problem loading the configuration file.");
        }

        FileProcessor fileProcessor = new FileProcessor(config, fileName);

        try {
            mappedFile = fileProcessor.getMappedFile();
            agronaManagementFactory = new AgronaManagementFactory(mappedFile);
            initialized = true;
        } catch (IllegalStateException e) {
            LOGGER.debug(e.toString());
        }

        return initialized;
    }

    /**
     * Returns a MetricsCollector instance
     * @return MetricsCollectorAgrona
     */
    public MetricsCollector getMetricsCollector() {
        CountersManagerEx createCountersManager = agronaManagementFactory.createCountersManager();
        return new MetricsCollectorAgrona(createCountersManager, fileName);
    }

    /**
     * Returns a MessagesCollector instance
     * @return MessagesCollectorAgrona
     */
    public MessagesCollector getMessagesCollector() {
        return new MessagesCollectorAgrona(agronaManagementFactory.createStringsManager());
    }
}
