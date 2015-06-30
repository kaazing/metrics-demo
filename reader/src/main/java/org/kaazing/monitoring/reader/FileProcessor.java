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

    private static final String LINUX_OS = "Linux";
    private static final String OS_NAME_SYSTEM_PROPERTY = "os.name";
    private static final String LINUX_DEV_SHM_DIRECTORY = "/dev/shm";

    public FileProcessor(Configuration config) {
        this.config = config;
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

        String fileName = config.get(Configuration.CFG_AGRONA_MONITORING_FILE);
        File tmpDir = new File(prefix + IoUtil.tmpDirName() + dirName, fileName);

        return IoUtil.mapExistingFile(tmpDir, fileName);
    }

}
