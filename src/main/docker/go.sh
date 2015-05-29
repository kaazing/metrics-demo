#!/bin/bash
export GATEWAY_OPTS=-Dorg.kaazing.gateway.management.AGRONA_ENABLED=true
bin/gateway.start &
sleep 10; java -jar /kaazing-metrics/metrics.viewer-develop-SNAPSHOT-jar-with-dependencies.jar
