# Simple Poc using Elasticsearch client with tracing

Goal was to replace the HttpClientBuilder used internally by the Elasticsearch library

For more context -> https://github.com/openzipkin/brave/issues/1226 


## Requirements
### Jaeger
```shell
docker run -d --name jaeger   -e COLLECTOR_ZIPKIN_HOST_PORT=:9411   -p 5775:5775/udp   -p 6831:6831/udp   -p 6832:6832/udp   -p 5778:5778   -p 16686:16686   -p 14268:14268   -p 14250:14250   -p 9411:9411   jaegertracing/all-in-one:1.27
```
### Elasticsearch
```shell
 docker run -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.15.1

```

