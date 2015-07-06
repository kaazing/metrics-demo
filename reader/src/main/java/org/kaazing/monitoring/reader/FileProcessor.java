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

import java.io.File;
import java.nio.MappedByteBuffer;

import uk.co.real_logic.agrona.IoUtil;

/**
 * Processes the Agrona monitoring file
 *
 */
public class FileProcessor {

    private Configuration config;

    private String fileName;
    private static final String LINUX_OS = "Linux";
    private static final String OS_NAME_SYSTEM_PROPERTY = "os.name";
    private static final String LINUX_DEV_SHM_DIRECTORY = "/dev/shm";

    public FileProcessor(Configuration config, String fileName) {
        this.config = config;
        this.fileName = fileName;
    }

    /**
     * Initializes and maps the Agrona monitoring file
     * @return MappedByteBuffer - the Agrona mapped file
     */
    public MappedByteBuffer getMappedFile() {
        String prefix = "";
        if (LINUX_OS.equalsIgnoreCase(System.getProperty(OS_NAME_SYSTEM_PROPERTY))) {
            prefix = LINUX_DEV_SHM_DIRECTORY;
        }

        String dirName = config.get(Configuration.CFG_AGRONA_MONITORING_DIR);

        if (fileName == null) {
            fileName = config.get(Configuration.CFG_AGRONA_MONITORING_FILE);
        }
        File tmpDir = new File(prefix + IoUtil.tmpDirName() + dirName, fileName);

        return IoUtil.mapExistingFile(tmpDir, fileName);
    }

}
