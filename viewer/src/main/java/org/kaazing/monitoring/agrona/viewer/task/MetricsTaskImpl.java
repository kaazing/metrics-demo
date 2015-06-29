package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.MetricsCollectorFactory;
import org.kaazing.monitoring.reader.api.MetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for the GetMetricsTask abstraction
 *
 */
public class MetricsTaskImpl implements MetricsTask{
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsTaskImpl.class);
    private MetricsCollector metricsCollector;
    private String fileName;
    private ScheduledFuture<?> task;

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor) {
        this.fileName = fileName;

        MetricsCollector metricsCollector = MetricsCollectorFactory.getInstanceForMonitoringFile(fileName);
        if (metricsCollector == null || !metricsCollector.initialize()) {
            LOGGER.error("There was a problem initializing the metrics reader. Exiting application.");
            System.exit(1);
        }

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
