#!/bin/bash

kubectl create configmap prometheus-cm --from-file acme/prometheus/prometheus.yml
kubectl create -f acme/prometheus.yaml
