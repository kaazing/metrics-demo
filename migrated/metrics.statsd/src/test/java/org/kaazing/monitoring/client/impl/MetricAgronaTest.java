/**
 * 
 */
package org.kaazing.monitoring.client.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 */
public class MetricAgronaTest {

    /**
     * Test method for {@link org.kaazing.monitoring.client.impl.MetricAgrona#formatForStatsD()}.
     */
    @Test
    public void testFormatForStatsD() {
        MetricAgrona metric = new MetricAgrona("test", 100);
        assertEquals(metric.formatForStatsD(), "test:100|c");
    }

}
