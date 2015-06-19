# Metrics
Metrics gathering and monitoring for Kaazing Gateway

# About this Project
This contains three different projects:
* metrics.statsd - used to send metrics from Agrona to a StatsD client
* metrics.viewer - used to see the metrics sent from Agrona 
* metrics.reader - common library used by statsd and viewer to read data from Agrona

# Minimum requirements for building the project
* Java Developer Kit (JDK) 8 
* Maven 3.0.5 or above

# Running this project
Once the gateway is running, the metrics.viewer can be run to view the monitoring counters exposed by the gateway or metrics.statsd can be used to push the monitoring counters to a statsd server.

Please check the metrics.viewer and metrics.reader components for instructions on how to run them.
