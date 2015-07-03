package org.kaazing.monitoring.reader.impl;

import uk.co.real_logic.agrona.concurrent.AtomicBuffer;
import uk.co.real_logic.agrona.concurrent.CountersManager;

/**
 * Wrapper over Agrona's Counter Manager to extend its functionality
 */
public class CountersManagerEx extends CountersManager {

    private AtomicBuffer valuesBuffer;

    /**
     * Create a new counter buffer manager over two buffers.
     *
     * @param labelsBuffer containing the human readable labels for the monitoring entities.
     * @param metricsBuffer containing the values of the String monitoring entities themselves.
     */
    public CountersManagerEx(AtomicBuffer labelsBuffer, AtomicBuffer valuesBuffer) {
        super(labelsBuffer, valuesBuffer);
        this.valuesBuffer = valuesBuffer;
    }

    /**
     * Returns the counter value for a given id
     *
     * @param id for which the value should be provided.
     * @return value of the counter. 
     */
    public Long getLongValueForId(int id) {
        int offset = CountersManager.counterOffset(id);

        return valuesBuffer.getLongVolatile(offset);
    }

}
