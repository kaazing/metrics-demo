/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
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
package org.kaazing.monitoring.reader.api;

import org.kaazing.monitoring.reader.exception.MetricsReaderException;
import org.kaazing.monitoring.reader.impl.MetricsFileProcessorImpl;

/**
 * This interface initializes the monitoring data
 */
public interface MetricsFileProcessor {

    static MetricsFileProcessor newInstance(String filename) {
        return new MetricsFileProcessorImpl(filename);
    }

    /**
     * Initializes monitoring data
     * @return boolean - returns true if the initialization was finished without any problem
     * @throws MetricsReaderException
     */
    boolean initialize() throws MetricsReaderException;

    /**
     * Returns a MMFReader instance
     * @return Metrics
     */
    Metrics getMetrics();
}
