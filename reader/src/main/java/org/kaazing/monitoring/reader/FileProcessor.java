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

import org.kaazing.monitoring.reader.file.location.MonitoringFolderAgrona;
import org.kaazing.monitoring.reader.impl.file.location.MonitoringFolderAgronaImpl;

import uk.co.real_logic.agrona.IoUtil;

/**
 * Processes the Agrona monitoring file
 *
 */
public class FileProcessor {

    private String fileName;
    private static MonitoringFolderAgrona agronaFolder = new MonitoringFolderAgronaImpl();

    public FileProcessor(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Initializes and maps the Agrona monitoring file
     * @return MappedByteBuffer - the Agrona mapped file
     */
    public MappedByteBuffer getMappedFile() {
        File file = new File(agronaFolder.getMonitoringDir(), fileName);

        return IoUtil.mapExistingFile(file, fileName);
    }

}
