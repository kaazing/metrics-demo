package org.kaazing.monitoring.agrona.viewer.task;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.kaazing.monitoring.reader.api.Metric;
import org.kaazing.monitoring.reader.api.MetricsCollector;

/**
 * Interface for tasks responsible with retrieving collector messages and metrics
 *
 */
public interface MetricsTask {

    /**
     * Getter for the file name
     * @return
     */
    String getFileName();

    /**
     * Getter for the metrics collector
     * @return
     */
    MetricsCollector getMetricsCollector();

    /**
     * Getter for the scheduled task
     * @return
     */
    ScheduledFuture<?> getScheduledTask();

    /**
     * Method performing cleanup
     */
    void cleanup();

    /**
     * Metrics items retrieval
     * @return
     */
    List<Metric> getMetricsList();

}
