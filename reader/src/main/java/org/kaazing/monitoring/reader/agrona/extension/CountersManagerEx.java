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
package org.kaazing.monitoring.reader.agrona.extension;

import uk.co.real_logic.agrona.concurrent.AtomicBuffer;
import uk.co.real_logic.agrona.concurrent.CountersManager;

/**
 * Wrapper over Agrona's Counter Manager to extend its functionality
 * This class was created to expose a public getValue method for a given id, since the Agrona
 * standard implementation did not provide one
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
