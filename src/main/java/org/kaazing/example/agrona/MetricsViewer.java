package org.kaazing.example.agrona;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.co.real_logic.agrona.IoUtil;
import uk.co.real_logic.agrona.concurrent.CountersManager;
import uk.co.real_logic.agrona.concurrent.SigInt;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

public class MetricsViewer {

    private static int COUNTER_LABELS_BUFFER_LENGTH = 32 * 1024 * 1024;
    private static int COUNTER_VALUES_BUFFER_LENGTH = 1024 * 1024;
    private static final String LINUX_OS = "Linux";
    private static final String OS_NAME_SYSTEM_PROPERTY = "os.name";
    private static final String LINUX_DEV_SHM_DIRECTORY = "/dev/shm";
	

    public static void main(String[] args) throws InterruptedException {
        String prefix = "";
        if (LINUX_OS.equalsIgnoreCase(System.getProperty(OS_NAME_SYSTEM_PROPERTY))) {
              prefix = LINUX_DEV_SHM_DIRECTORY;
        }
        File tmpDir = new File(prefix + IoUtil.tmpDirName() + "/kaazing", "monitor");
        MappedByteBuffer mapNewFile = IoUtil.mapExistingFile(tmpDir, "monitor");

        UnsafeBuffer labelsBuffer = new UnsafeBuffer(mapNewFile, 64, COUNTER_LABELS_BUFFER_LENGTH);
        UnsafeBuffer valuesBuffer =
                new UnsafeBuffer(mapNewFile, 64 + COUNTER_LABELS_BUFFER_LENGTH, COUNTER_VALUES_BUFFER_LENGTH);

        CountersManager countersManager = new CountersManager(labelsBuffer, valuesBuffer);

        final AtomicBoolean running = new AtomicBoolean(true);
        SigInt.register(() -> running.set(false));

        while (running.get()) {
            System.out.println("Agrona counters:");
            System.out.format("%1$tH:%1$tM:%1$tS - Gateway monitor\n", new Date());
            System.out.println("=========================");
            System.out.println("Counter id: Counter value - Counter name");

            countersManager.forEach((id, label) -> {
                final int offset = CountersManager.counterOffset(id);
                final long value = valuesBuffer.getLongVolatile(offset);

                System.out.format("%3d: %,20d - %s\n", id, value, label);
            });

            System.out.println("");

            Thread.sleep(1000);
        }
    }

}