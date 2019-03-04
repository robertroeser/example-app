#!/bin/bash

app="netifi-broker"
nodeName=$(kubectl get pods --selector "app=${app}" -o jsonpath='{.items[].spec.nodeName}')
externalIP=$(kubectl get node ${nodeName} -o jsonpath='{.status.addresses[?(@.type=="ExternalIP")].address}')

pushd ../../
./gradlew :client:run -Dnetifi.proteus.broker.hostname=${externalIP} -Dnetifi.proteus.broker.port=8001
popd
