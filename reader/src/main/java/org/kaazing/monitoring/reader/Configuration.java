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
package org.kaazing.monitoring.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for getting Agrona configuration values
 */
public class Configuration {

    public static final String CFG_AGRONA_MONITORING_DIR = "metrics.reader.agrona.monitoring.dir.name";

    private Properties properties;
    private static final String CONFIG_FILE_NAME = "metrics.reader.config.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    /**
     * Method that loads the configuration file
     * @return boolean (false if the file is not loaded properly)
     */
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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("Exception caught while reading the configuration file: ", e);
            }
            else {
                LOGGER.error("Exception caught while reading the configuration file: ", e.getMessage());
            }
            return false;
        }
        return true;
    }

    /**
     * Returns the value for the given property name
     * @param name
     * @return String
     */
    public String get(String value) {
        return properties.getProperty(value);
    }

    /**
     * Returns the value for the given property name; in case the property doesn't exist, it returns the default value
     * @param name
     * @return String
     */
    public String get(String value, String defaultValue) {
        return properties.getProperty(value, defaultValue);
    }

}
