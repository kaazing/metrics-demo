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

package org.kaazing.monitoring.reader.file.location.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kaazing.monitoring.reader.Configuration;
import org.kaazing.monitoring.reader.file.location.MonitoringFolderAgrona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.real_logic.agrona.IoUtil;

public class MonitoringFolderAgronaImpl implements MonitoringFolderAgrona {
    private static final String LINUX_OS = "Linux";
    private static final String OS_NAME_SYSTEM_PROPERTY = "os.name";
    private static final String LINUX_DEV_SHM_DIRECTORY = "/dev/shm";
    private static final Configuration config = new Configuration();
    private List<String> files = new ArrayList<String>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringFolderAgronaImpl.class);

    @Override
    public List<String> getMonitoringFiles() {
        files.clear();
        File directory = new File(getMonitoringDir());

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file.getName());
            }
        }
        return files;
    }

    @Override
    public String getMonitoringDir() {
        if (!config.loadConfigFile()) {
            LOGGER.error("There was a problem with the configuration file. Exiting application.");
            return null;
        }
        String prefix = getOSPrefix();
        return prefix + IoUtil.tmpDirName() + config.get(Configuration.CFG_AGRONA_MONITORING_DIR);
    }

    /**
     * Method returning OS prefix
     * @return - the computed OS prefix: shared memory location for linux, void String otherwise
     */
    private String getOSPrefix() {
        String prefix = "";
        if (LINUX_OS.equalsIgnoreCase(System.getProperty(OS_NAME_SYSTEM_PROPERTY))) {
            prefix = LINUX_DEV_SHM_DIRECTORY;
        }
        return prefix;
    }
}
