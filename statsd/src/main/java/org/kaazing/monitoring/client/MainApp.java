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

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.Metrics;
import org.kaazing.monitoring.reader.api.MetricsFileProcessor;
import org.kaazing.monitoring.reader.api.ServiceCounters;
import org.kaazing.monitoring.reader.exception.MetricsReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.real_logic.agrona.concurrent.SigInt;

public class MainApp {

    private static final int DEFAULT_UPDATE_INTERVAL = 1000;
    private static final String DEFAULT_SEPARATOR = ".";

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) {

        if (args.length == 0) {
            LOGGER.error("Please provide a gateway identifier for the StatsD publisher. Exiting application.");
            System.exit(1);
        }

        int exitCode = 0;

        Configuration config = new Configuration();

        if (!config.loadConfigFile()) {
            LOGGER.error("There was a problem with the configuration file. Exiting application.");
            System.exit(1);
        }

        String hostname = config.get(Configuration.CFG_STATSD_HOST);
        int port = Integer.parseInt(config.get(Configuration.CFG_STATSD_PORT));
        int updateInterval =
                Integer.parseInt(config.get(Configuration.CFG_UPDATE_INTERVAL, Integer.toString(DEFAULT_UPDATE_INTERVAL)));

        MetricsFileProcessor monitoringDataProcessor = MetricsFileProcessor.newInstance(args[0]);

        // Waits until the metrics file is created by the producer (e.g. this app is started and then waits for a
        // gateway to start and create the file)
        try {
            while (!monitoringDataProcessor.initialize()) {
                try {
                    Thread.sleep(updateInterval);
                } catch (InterruptedException e) {
                    LOGGER.debug("An InterruptedException was caught while trying to initialize metricsCollector: ", e);
                    if (!monitoringDataProcessor.initialize()) {
                        System.exit(1);
                    }
                }
            }
        } catch (MetricsReaderException e) {
            LOGGER.error("There was a problem with the metrics.reader configuration file. Exiting application.");
        }

        Metrics reader = monitoringDataProcessor.getMMFReader();

        if (reader == null) {
            LOGGER.error("There was a problem initializing the metrics reader. Exiting application.");
            System.exit(1);
        }

        ScheduledExecutorService taskExecutor = Executors.newScheduledThreadPool(1);
        SigInt.register(() -> taskExecutor.shutdown());

        try {
            final StatsdPublisher client = new StatsdPublisher(hostname, port);
            String gatewayId = reader.getGateway().getGatewayId();

            taskExecutor.scheduleAtFixedRate(() -> {
                // Gets the list of all existing services
                    for (ServiceCounters service : reader.getServices()) {
                        // Gets the list of all counters for a given service and sends them to the StatsD publisher
                        for (Counter counter : service.getCounters()) {
                            // c - simple counter for StatsD
                            String counterName =
                                    gatewayId + DEFAULT_SEPARATOR + service.getName() + DEFAULT_SEPARATOR + counter.getLabel();
                            client.send(String.format(Locale.ENGLISH, "%s:%s|c", counterName, counter.getValue()));
                        }
                    }
                    // Gets the list of all gateway counters and sends them to the StatsD publisher
                    for (Counter counter : reader.getGateway().getCounters()) {
                        String counterName = gatewayId + DEFAULT_SEPARATOR + counter.getLabel();
                        client.send(String.format(Locale.ENGLISH, "%s:%s|c", counterName, counter.getValue()));
                    }
                }, 0, updateInterval, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("There was a problem initializing the StatsD publisher. Exiting application.", e);
            exitCode = 1;
            taskExecutor.shutdown();
        }

        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            LOGGER.debug("An InterruptedException was caught while waiting termination of the taskExecutor: ", e);
        }
        System.exit(exitCode);
    }
}
