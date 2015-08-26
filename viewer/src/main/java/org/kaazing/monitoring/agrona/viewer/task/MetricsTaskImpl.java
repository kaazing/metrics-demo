package org.kaazing.monitoring.agrona.viewer.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.agrona.viewer.MetricsViewer;
import org.kaazing.monitoring.reader.api.Metrics;
import org.kaazing.monitoring.reader.api.Counter;
import org.kaazing.monitoring.reader.api.ServiceCounters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation for the GetMetricsTask abstraction
 * This implementation is responsible with retrieving collector messages and metrics
 * and adding them directly to the logger output.
 *
 */
public class MetricsTaskImpl implements MetricsTask {
    private String fileName;
    private ScheduledFuture<?> task;
    private static final String DEFAULT_SEPARATOR = ".";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsTaskImpl.class);

    public MetricsTaskImpl(String fileName, ScheduledExecutorService taskExecutor, Metrics reader) {
        this.fileName = fileName;
        String gatewayId = reader.getGateway().getGatewayId();
        task = taskExecutor.scheduleAtFixedRate(() -> {
                for (ServiceCounters service : reader.getServices()) {
                    for (Counter counter : service.getCounters()) {
                            String counterName =
                                    gatewayId + DEFAULT_SEPARATOR + service.getName() + DEFAULT_SEPARATOR + counter.getLabel();
                            LOGGER.debug("{} - {}", counter.getValue(), counterName);
                    }
                }
                for (Counter counter : reader.getGateway().getCounters()) {
                    String counterName = gatewayId + DEFAULT_SEPARATOR + counter.getLabel();
                    LOGGER.debug("{} - {}", counter.getValue(), counterName);
                }
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
