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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.kaazing.monitoring.reader.file.location.MonitoringFolderAgrona;
import org.kaazing.monitoring.reader.impl.file.location.MonitoringFolderAgronaImpl;

import uk.co.real_logic.agrona.IoUtil;

public class MonitoringFolderAgronaImplTest {
    private static final String MONITORING_FILE_LOCATION = "/kaazing";

    @Test
    public void testGetMonitoringFilesShouldReturnEmptyList() {
        MonitoringFolderAgrona monitoringFolder = new MonitoringFolderAgronaImpl();
        List<String> files = monitoringFolder.getMonitoringFiles();
        assertNotNull(files);
        //no monitoring files should be present by default
        assertEquals(0, files.size());
    }

    @Test
    public void testGetMonitoringFilesShouldNonEmptyList() {
        MonitoringFolderAgrona monitoringFolder = new MonitoringFolderAgronaImpl();
        String folder = monitoringFolder.getMonitoringDir();
        File file1 = new File(folder + "/test1");
        File file2 = new File(folder + "/test2");
        try {
            file1.createNewFile();
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> files = monitoringFolder.getMonitoringFiles();
        assertNotNull(files);
        //no monitoring files should be present by default
        assertEquals(2, files.size());
        //cleanup files
        file1.delete();
        file2.delete();
    }

    @Test
    public void testGetMonitoringDir() {
        MonitoringFolderAgrona monitoringFolder = new MonitoringFolderAgronaImpl();
        String monitoringDir = "";
        if ("Linux".equals(System.getProperty("os.name"))) {
            monitoringDir = "/dev/shm" + IoUtil.tmpDirName() + MONITORING_FILE_LOCATION;
        }
        else {
            monitoringDir = IoUtil.tmpDirName() + MONITORING_FILE_LOCATION;
        }
        assertEquals(monitoringDir , monitoringFolder.getMonitoringDir());
    }
}
