#!/bin/zsh

kubectl delete services consumer

kubectl delete deployment consumer

mvn spring-boot:build-image

kubectl apply -f deploy-config.yaml

kubectl get pod -o wide