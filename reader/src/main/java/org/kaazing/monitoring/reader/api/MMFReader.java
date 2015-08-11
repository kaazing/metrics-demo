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

package org.kaazing.monitoring.reader.api;

import java.util.List;

/**
 * This interface is used for reading data from the MMF
 */
public interface MMFReader {

    /**
     * Returns the gateway id
     * @return String
     */
    String getGatewayId();

    /**
     * Returns the MMF's version
     * @return int
     */
    int getFileVersion();

    /**
     * Returns a list with all the collected gateway counters
     * @return List<Counter>
     */
    List<Counter> getGatewayCounters();

    /**
     * Returns a list with all the existing services
     * @return List<Service>
     */
    List<ServiceCounters> getServices();

    /**
     * Returns a list with all the collected service counters for a given service
     * @param service
     * @return List<Counter>
     */
    List<Counter> getServiceCounters(ServiceCounters service);

}
