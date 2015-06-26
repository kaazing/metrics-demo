package org.kaazing.example.agrona;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.kaazing.monitoring.reader.MetricsCollectorFactory;
import org.kaazing.monitoring.reader.api.MetricsCollector;

import uk.co.real_logic.agrona.concurrent.SigInt;

public class MetricsViewer {

    private static final int UPDATE_INTERVAL = 1000;

    public static void main(String[] args) throws InterruptedException {

        MetricsCollector metricsCollector = MetricsCollectorFactory.getInstance(args[0]);

        if (metricsCollector == null) {
            System.out.println("There was a problem initializing the metrics reader. Exiting application.");
            System.exit(1);
        }

        // Waits until the metrics file is created by the producer (e.g. this app is started and then waits for a
        // gateway to start and create the file)
        while (!metricsCollector.initialize()) {
            Thread.sleep(UPDATE_INTERVAL);
        }

        ScheduledExecutorService taskExecutor = Executors.newScheduledThreadPool(1);
        SigInt.register(() -> taskExecutor.shutdown());

        taskExecutor.scheduleAtFixedRate(() -> {
            System.out.println("Agrona counters:");
            System.out.format("%1$tH:%1$tM:%1$tS - Gateway monitor\n", new Date());
            System.out.println("=========================");
            System.out.println("Counter id: Counter value - Counter name");

            metricsCollector.getMetrics();

            System.out.println("");
        }, 0, UPDATE_INTERVAL, TimeUnit.MILLISECONDS);

        taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}
