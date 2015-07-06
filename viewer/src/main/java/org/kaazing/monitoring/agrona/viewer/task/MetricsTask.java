package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledFuture;

import org.kaazing.monitoring.reader.api.MessagesCollector;
import org.kaazing.monitoring.reader.api.MetricsCollector;

/**
 * Interface for tasks defined with getMetrics
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
     * Messages collector getter
     * @return
     */
    MessagesCollector getMessagesCollector();

}
