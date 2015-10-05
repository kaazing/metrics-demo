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

import java.nio.MappedByteBuffer;
import java.util.List;

import org.kaazing.monitoring.reader.impl.MMFReaderBuilderImpl;

/**
 * This interface is used for reading data from the metrics data file
 */
public interface Metrics {

    /**
     * Creates an object allowing gateway metrics to be read
     * @param buffer Memory mapped buffer over the metrics data file
     * @return An object allowing all Gateway and service metrics to be read 
     *      repeatedly without causing garbage collection
     */
    static Metrics wrap(MappedByteBuffer buffer) {
        return new MMFReaderBuilderImpl(buffer).build();
    }

    /**
     * Returns the version of the metrics data file format
     * @return int
     */
    int getMetricsVersion();

    /**
     * Returns an object giving access to Gateway level metrics
     * @return String
     */
    GatewayCounters getGateway();

    /**
     * Returns a list of objects giving access to service-level metrics
     * @return List<ServiceCounters>  List of service metrics objects
     */
    List<ServiceCounters> getServices();

}
