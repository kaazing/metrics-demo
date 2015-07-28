package org.kaazing.monitoring.agrona.viewer.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.api.Metric;
import org.kaazing.monitoring.reader.api.MetricsCollector;

/**
 * Implementation for the GetMetricsTask abstraction
 * This implementation is responsible with retrieving collector messages and metrics
 * and adding them directly to the logger output.
 *
 */
public class MetricsTaskImpl implements MetricsTask{
    private MetricsCollector metricsCollector;
    private List<Metric> metricsList = new ArrayList<Metric>();
    private String fileName;
    private ScheduledFuture<?> task;

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor, MetricsCollector metricsCollector) {
        this.fileName = fileName;
        this.metricsCollector = metricsCollector;
        task = taskExecutor.scheduleAtFixedRate(() -> {
            // metrics retrieved - log message directly added to the logger output for each metric (using default logging in metricsCollector.getMetrics() method)
            metricsList = metricsCollector.getMetrics();
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
    public List<Metric> getMetricsList() {
        return metricsList;
    }

    @Override
    public void cleanup() {
        task.cancel(true);
    }
}
