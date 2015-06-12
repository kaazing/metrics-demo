# metrics
Metrics gathering and monitoring for Kaazing Gateway

```
git clone https://github.com/kaazing/gateway.git
pushd gateway
mvn clean install -Pdocker
popd

git clone https://github.com/kaazing/metrics.git
pushd metrics
docker build -t kaazing/gateway-with-metrics .
docker run -v /dev/urandom:/dev/random -h gateway -p 8000:8000 kaazing/gateway-with-metrics
```

Add `gateway` to your `/etc/hosts` file pointing to the Docker host.

Point your browser at `http://gateway:8000`
