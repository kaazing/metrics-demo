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

package org.kaazing.monitoring.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {

    public static final String CFG_AGRONA_MONITORING_DIR = "metrics.reader.agrona.monitoring.dir.name";
    public static final String CFG_AGRONA_MONITORING_FILE = "metrics.reader.agrona.monitoring.file.name";

    private Properties properties;
    private static final String CONFIG_FILE_NAME = "metrics.reader.config.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    public boolean loadConfigFile() {
        properties = new Properties();
        InputStream inputStream = null;

        inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if (inputStream == null) {
            LOGGER.error("Unable to find configuration file: " + CONFIG_FILE_NAME);
            return false;
        }

        try {
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    public String get(String value) {
        return properties.getProperty(value);
    }

    public String get(String value, String defaultValue) {
        return properties.getProperty(value, defaultValue);
    }

}
