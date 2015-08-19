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

import java.util.ArrayList;
import java.util.List;

import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsCollectorAgrona implements MetricsCollector {

    private CountersManagerEx countersManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsCollectorAgrona.class);

    public MetricsCollectorAgrona(CountersManagerEx countersManager) {
        this.countersManager = countersManager;
    }

    @Override
    public List<Counter> getCounters() {
        List<Counter> counters = new ArrayList<Counter>();

        if (countersManager != null) {
            countersManager.forEach((id, label) -> {
                final long value = countersManager.getLongValueForId(id);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("%3d: %,20d - %s", id, value, label));
                }

                counters.add(new CounterImpl(label, value));
            });
        }

        return counters;
    }
}
