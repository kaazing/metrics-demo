package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.api.MMFReader;
import org.kaazing.monitoring.reader.api.ServiceCounters;

/**
 * Implementation for the GetMetricsTask abstraction
 * This implementation is responsible with retrieving collector messages and metrics
 * and adding them directly to the logger output.
 *
 */
public class MetricsTaskImpl implements MetricsTask {
    private String fileName;
    private ScheduledFuture<?> task;

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor, MMFReader reader) {
        this.fileName = fileName;
        task = taskExecutor.scheduleAtFixedRate(() -> {
            // metrics retrieved - log message directly added to the logger output for each metric (using default
            // logging in metricsCollector.getServiceCounters() method)
                for (ServiceCounters service : reader.getServices()) {
                    service.getCounters();
                }
                reader.getGateway().getCounters();
            }, 0, MetricsViewer.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void cleanup() {
        task.cancel(true);
    }
}
