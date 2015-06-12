FROM kaazing/gateway:latest

RUN mkdir /kaazing-metrics
ADD viewer/target/metrics.viewer-develop-SNAPSHOT-jar-with-dependencies.jar /kaazing-metrics/
ADD src/main/docker/go.sh /kaazing-metrics/

CMD ["/kaazing-metrics/go.sh"]
