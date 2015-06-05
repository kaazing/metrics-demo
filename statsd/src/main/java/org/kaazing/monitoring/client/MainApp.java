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

package org.kaazing.monitoring.client;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.kaazing.monitoring.reader.MetricsCollectorFactory;
import org.kaazing.monitoring.reader.api.Metric;
import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp {

    private static final int DEFAULT_UPDATE_INTERVAL = 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {

        StatsdPublisher client = null;
        Configuration config = new Configuration();

        if (!config.loadConfigFile()) {
            LOGGER.error("There was a problem with the configuration file. Exiting application.");
            System.exit(1);
        }

        String hostname = config.get(Configuration.CFG_STATSD_HOST);
        int port = Integer.parseInt(config.get(Configuration.CFG_STATSD_PORT));
        int updateInterval =
                Integer.parseInt(config.get(Configuration.CFG_UPDATE_INTERVAL, Integer.toString(DEFAULT_UPDATE_INTERVAL)));

        try {
            client = new StatsdPublisher(hostname, port);
        } catch (IOException e) {
            LOGGER.error(String.format("There was a problem initializing the StatsD publisher. Exiting application.", e));
            System.exit(1);
        }

        final AtomicBoolean running = new AtomicBoolean(true);

        MetricsCollector metricsCollector = MetricsCollectorFactory.getInstance();

        if (metricsCollector == null) {
            LOGGER.error("There was a problem initializing the metrics reader. Exiting application.");
            System.exit(1);
        }

        try {
            // Waits until the metrics file is created
            while (metricsCollector.initialize() == false) {
                Thread.sleep(updateInterval);
            }

            // Gets the list of all metrics and sends it to the StatsD publisher
            while (running.get()) {
                for (Metric metric : metricsCollector.getMetrics()) {
                    // c - simple counter for StatsD
                    client.send(String.format(Locale.ENGLISH, "%s:%s|c", metric.getName(), metric.getValue()));
                }

                Thread.sleep(updateInterval);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
