# metrics.statsd

# About this Project
StatsD implementation of metrics collection from Agrona

# Building this Project

## Minimum requirements for building the project
* Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 8 
* Maven 3.0.5 or above

## Steps for building this project
`mvn clean install`

## Steps for running this project
0. `cd target`
1. Unpack the appropriate distribution

   Mac/Linux: `tar -xvf metrics.statsd-develop-SNAPSHOT-unix.tar.gz`

   Windows: `unzip metrics.statsd-develop-SNAPSHOT-windows.zip`
2. Start the StatsD publisher 

   `cd metrics.statsd-develop-SNAPSHOT/bin`

   Mac/Linux: `./metrics.statsd.start GATEWAY_IDENTIFIER`

   Windows: `metrics.statsd.start.bat GATEWAY_IDENTIFIER`

## Notes
Where GATEWAY_IDENTIFIER is an environment variable, representing the identifier of the gateway that needs monitoring.

In order for the project to run, the "monitoring" alias must be mapped to a valid IP.
