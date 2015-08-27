cd %~dp0
java -XX:+HeapDumpOnOutOfMemoryError -jar ../lib/metrics.statsd-develop-SNAPSHOT.jar %*
