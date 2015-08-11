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

import java.util.List;

import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.GatewayCounters;
import org.kaazing.monitoring.reader.api.MMFReader;
import org.kaazing.monitoring.reader.api.ServiceCounters;

public class MMFReaderImpl implements MMFReader {

    private GatewayCounters gateway;
    private List<ServiceCounters> services;
    private int fileVersion;

    public MMFReaderImpl(int fileVersion, GatewayCounters gateway, List<ServiceCounters> services) {
        this.fileVersion = fileVersion;
        this.gateway = gateway;
        this.services = services;
    }

    @Override
    public String getGatewayId() {
        return gateway.getGatewayId();
    }

    @Override
    public int getFileVersion() {
        return fileVersion;
    }

    @Override
    public List<ServiceCounters> getServices() {
        return services;
    }

    @Override
    public List<Counter> getGatewayCounters() {
        return gateway.getCounters();
    }

    @Override
    public List<Counter> getServiceCounters(ServiceCounters service) {
        return service.getCounters();
    }
}
