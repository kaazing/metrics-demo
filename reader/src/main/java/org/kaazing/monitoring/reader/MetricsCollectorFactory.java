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

import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.kaazing.monitoring.reader.impl.MetricsCollectorAgrona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class that returns a MetricsCollector instance
 */
public class MetricsCollectorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollectorFactory.class);
    private static final Configuration config = new Configuration();

    /**
     * Initializes a MetricCollector instance for gateway instance
     * @param gatewayId
     * @return MetricsCollectorAgrona
     */
    public static MetricsCollector getInstanceForGateway(String gatewayId) {
        if (!config.loadConfigFile()) {
            LOGGER.error("There was a problem with the configuration file. Exiting application.");
            return null;
        }

        return new MetricsCollectorAgrona(config, gatewayId);
    }

    /**
     * Initializes a MetricCollector instance for monitoring file
     * @param fileName
     * @return MetricsCollectorAgrona
     */
    public static MetricsCollector getInstanceForMonitoringFile(String fileName) {
        return new MetricsCollectorAgrona(fileName);
    }
}
