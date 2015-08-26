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

public class MetricsCollectorAgrona implements MetricsCollector {

    private List<Counter> counters;

    public MetricsCollectorAgrona(CountersManagerEx countersManager) {
        createCountersList(countersManager);
    }

    @Override
    public List<Counter> getCounters() {
        return counters;
    }

    /**
     * Creates the counters list; this method is only called once, from the constructor, 
     * so it doesn't cause a lot of garbage collection and reduce performance
     * @param countersManager
     */
    private void createCountersList(CountersManagerEx countersManager) {
        counters = new ArrayList<Counter>();

        if (countersManager != null) {
            countersManager.forEach((id, label) -> {
                counters.add(new CounterImpl(label, id, countersManager));
            });
        }
    }
}
