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

import java.util.concurrent.atomic.AtomicBoolean;

import org.kaazing.monitoring.client.api.Metric;
import org.kaazing.monitoring.client.api.MetricsCollector;
import org.kaazing.monitoring.client.impl.MetricsCollectorAgrona;

public class MainApp {

    private static final String IP = "monitoring";
    private static final int PORT = 8125;

    public static void main(String[] args) {

        StatsdPublisher client;

        try {
            client = new StatsdPublisher(IP, PORT);
            final AtomicBoolean running = new AtomicBoolean(true);

            MetricsCollector metricsCollector = new MetricsCollectorAgrona();
            // Gets the list of all metrics and sends it to the StatsD publisher
            while (running.get()) {
                for (Metric metric : metricsCollector.getMetrics()) {
                    client.send(metric.formatForStatsD());
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
