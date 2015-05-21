# metrics.statsd

# About this Project
StatsD implementation of metrics collection from Agrona

# Building this Project

## Minimum requirements for building the project
* Java Developer Kit (JDK) or Java Runtime Environment (JRE) Java 8 
* Maven 3.0.5

## Steps for building this project
mvn clean install

## Steps for running this project
java -jar target/MetricsPublisher-0.0.1-SNAPSHOT-jar-with-dependencies.jar

## Note
In order for the project to run, the "monitoring" alias must be mapped to a valid IP.
