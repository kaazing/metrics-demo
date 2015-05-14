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

package metrics;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

import uk.co.real_logic.agrona.IoUtil;
import uk.co.real_logic.agrona.concurrent.CountersManager;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class MetricsCollectorAgrona implements MetricsCollector {

    private static int COUNTER_LABELS_BUFFER_LENGTH = 1024 * 1024;
    private static int COUNTER_VALUES_BUFFER_LENGTH = 1024 * 1024;

    private CountersManager countersManager;
    private UnsafeBuffer valuesBuffer;
    private UnsafeBuffer labelsBuffer;

    /**
     * Constructor for configuring Agrona
     */
    public MetricsCollectorAgrona() {
        File tmpDir = new File(IoUtil.tmpDirName() + "agrona", "test");
        MappedByteBuffer mapNewFile = IoUtil.mapExistingFile(tmpDir, "test");

        labelsBuffer = new UnsafeBuffer(mapNewFile, 0, COUNTER_LABELS_BUFFER_LENGTH);
        valuesBuffer = new UnsafeBuffer(mapNewFile, COUNTER_LABELS_BUFFER_LENGTH, COUNTER_VALUES_BUFFER_LENGTH);

        countersManager = new CountersManager(labelsBuffer, valuesBuffer);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> metrics = new ArrayList<Metric>();
        countersManager.forEach((id, label) -> {

            final int offset = CountersManager.counterOffset(id);
            final long value = valuesBuffer.getLongVolatile(offset);
            System.out.format("%3d: %,20d - %s\n", id, value, label);

            metrics.add(new MetricAgrona(label, value));
        });

        return metrics;
    }

}
