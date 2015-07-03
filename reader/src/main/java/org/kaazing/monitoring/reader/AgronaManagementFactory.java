package org.kaazing.monitoring.reader;

import java.nio.MappedByteBuffer;

import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.agrona.extension.StringsManager;

import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

/**
 * Factory class for creating managers for Agrona management
 */
public class AgronaManagementFactory {
    private static int COUNTER_LABELS_BUFFER_LENGTH = 32 * 1024 * 1024;
    private static int COUNTER_VALUES_BUFFER_LENGTH = 1024 * 1024;

    private static int STRING_LABELS_BUFFER_LENGTH = 32 * 1024 * 1024;
    private static int STRING_VALUES_BUFFER_LENGTH = 32 * 1024 * 1024;

    private static int METADATA_BUFFER_LENGTH = 64;

    MappedByteBuffer mappedByteBuffer;

    private CountersManagerEx countersManager;
    private StringsManager stringsManager;

    public AgronaManagementFactory(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    public CountersManagerEx createCountersManager() {
        UnsafeBuffer counterLabelsBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH, COUNTER_LABELS_BUFFER_LENGTH);
        UnsafeBuffer counterValuesBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH + COUNTER_LABELS_BUFFER_LENGTH,
                        COUNTER_VALUES_BUFFER_LENGTH);

        countersManager = new CountersManagerEx(counterLabelsBuffer, counterValuesBuffer);
        return countersManager;
    }

    public StringsManager createStringsManager() {
        UnsafeBuffer stringLabelsBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH + COUNTER_LABELS_BUFFER_LENGTH
                        + COUNTER_VALUES_BUFFER_LENGTH, STRING_LABELS_BUFFER_LENGTH);
        UnsafeBuffer stringValuesBuffer =
                new UnsafeBuffer(mappedByteBuffer, METADATA_BUFFER_LENGTH + COUNTER_LABELS_BUFFER_LENGTH
                        + COUNTER_VALUES_BUFFER_LENGTH + STRING_LABELS_BUFFER_LENGTH, STRING_VALUES_BUFFER_LENGTH);

        stringsManager = new StringsManager(stringLabelsBuffer, stringValuesBuffer);
        return stringsManager;
    }

}
