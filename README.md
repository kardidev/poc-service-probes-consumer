# poc-service-probes-consumer

A service, which is supposed to emulate load via HTTP with different weight.
Task is set by calling a simple REST API.

This repository is a part of a project, which is meant to test spring-actuator health end-points.

## API to emulate load:

Deploy frontservice only with liveness probe configured

```$xslt
kubectl apply -f liveness-service-deploy-config.yaml
```
Run watcher in a separate terminal window
```$xslt
watch -t -n 1 ./watcher.sh
```
Run loader in a separate terminal window
```$xslt
./loader.sh 80 5500 1
```
Check whether any request was rejected. Reduce a weight of request to 5000 if it happens.

Run loader once again and in parallel run the following command two times to add extra load for one of the pods.
Use an existing pod IP you can obtain via "kubectl get pods".
```$xslt
curl -i 'http://localhost:8082/consumer/load/10.1.0.130?weight=59000'
```
Some requests most probably will be rejected.

Deploy frontservice with both liveness and readiness probes configured.
```$xslt
kubectl apply -f health-service-deploy-config.yaml 
```
Perform the test with extra load once again and make sure that the distribution is much better now, which leads to having less rejections.

Try to configure horizontal auto-scaling for your frontservice deployment using custom metric to scale up, when there are no available pods.