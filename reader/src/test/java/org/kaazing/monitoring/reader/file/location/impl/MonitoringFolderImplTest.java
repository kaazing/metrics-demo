/**
 * Copyright 2007-2016, Kaazing Corporation. All rights reserved.
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
package org.kaazing.monitoring.reader.file.location.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.kaazing.monitoring.reader.file.location.MonitoringFolder;
import org.kaazing.monitoring.reader.impl.file.location.MonitoringFolderImpl;

import uk.co.real_logic.agrona.IoUtil;

public class MonitoringFolderImplTest {
    private static final String DEV_SHM = "/dev/shm";
    private static final String OS_NAME = "os.name";
    private static final String LINUX = "Linux";
    private static final String MONITORING_FILE_LOCATION = "/kaazing";
    private static final long TIMESTAMP = (new Date()).getTime();

    @Test
    public void getMonitoringFilesShouldReturnEmptyList() {
        MonitoringFolder monitoringFolder = new MonitoringFolderAgronaImplMonitoringDirMocked();
        List<String> files = monitoringFolder.getMonitoringFiles();
        assertNotNull(files);
        //no monitoring files should be present by default
        assertEquals(0, files.size());
    }

    @Test
    public void getMonitoringFilesShouldReturnNonEmptyList() {
        MonitoringFolder monitoringFolder = new MonitoringFolderAgronaImplMonitoringDirMocked();
        String folder = monitoringFolder.getMonitoringDir();
        File directory = new File(folder);
        File file1 = new File(folder + "/test1");
        File file2 = new File(folder + "/test2");
        try {
            boolean created = directory.mkdirs();
            assertEquals(true, created);
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

        }
        finally {
            // always cleanup files, even if there is an exception
            file1.delete();
            file2.delete();
            directory.delete();
        }
    }

    @Test
    public void getMonitoringDirShouldReturnConfiguredDirectoryNameInCorrectLocation() {
        MonitoringFolder monitoringFolder = new MonitoringFolderImpl();
        String monitoringDir = "";
        if (LINUX.equals(System.getProperty(OS_NAME))) {
            monitoringDir = DEV_SHM + IoUtil.tmpDirName() + MONITORING_FILE_LOCATION;
        }
        else {
            monitoringDir = IoUtil.tmpDirName() + MONITORING_FILE_LOCATION;
        }
        assertEquals(monitoringDir , monitoringFolder.getMonitoringDir());
    }

    private class MonitoringFolderAgronaImplMonitoringDirMocked extends MonitoringFolderImpl {
        @Override
        public String getMonitoringDir() {
            String prefix = "";
            if (LINUX.equalsIgnoreCase(System.getProperty(OS_NAME))) {
                prefix = DEV_SHM;
            }
            return prefix + IoUtil.tmpDirName() + "KaazingTest" + TIMESTAMP;
        }
    }
}
