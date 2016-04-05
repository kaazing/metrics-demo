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
package org.kaazing.monitoring.reader.impl;

import org.kaazing.monitoring.reader.agrona.extension.CountersManagerEx;
import org.kaazing.monitoring.reader.api.Counter;

public class CounterImpl implements Counter {

    private String label;
    private int id;
    private CountersManagerEx countersManager;

    public CounterImpl(String label, int id, CountersManagerEx countersManager) {
        this.label = label;
        this.id = id;
        this.countersManager = countersManager;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public long getValue() {
        return countersManager.getLongValueForId(id);
    }

}
