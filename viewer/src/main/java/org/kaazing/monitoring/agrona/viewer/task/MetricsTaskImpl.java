package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.api.MetricsCollector;

/**
 * Implementation for the GetMetricsTask abstraction
 *
 */
public class MetricsTaskImpl implements MetricsTask{
    private MetricsCollector metricsCollector;
    private String fileName;
    private ScheduledFuture<?> task;

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor, MetricsCollector metricsCollector) {
        this.fileName = fileName;
        this.metricsCollector = metricsCollector;

        @SuppressWarnings("unused")
        ScheduledFuture<?> task = taskExecutor.scheduleAtFixedRate(() -> {
            metricsCollector.getMetrics();
        }, 0, MetricsViewer.UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public MetricsCollector getMetricsCollector() {
        return metricsCollector;
    }

    @Override
    public ScheduledFuture<?> getScheduledTask() {
        return task;
    }

    @Override
    public void cleanup() {
        task.cancel(true);
    }
}
