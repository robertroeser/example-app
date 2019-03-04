#!/bin/bash
set -e
sudo apt-get update
sudo apt-get install default-jre

curl https://www-us.apache.org/dist//jmeter/binaries/apache-jmeter-5.0.tgz --output jmeter.tgz
tar -xvzf jmeter.tgz
rm jmeter.tgz

cp ../../runner/jmeter/jmeter-proteus-plugin.jar ./apache-jmeter-5.0/lib/ext/jmeter-proteus-plugin.jar
cp -R ../../runner/jmeter/jmeter-config/* ./apache-jmeter-5.0/bin/

app="netifi-broker"
nodeName=$(kubectl get pods --selector "app=${app}" -o jsonpath='{.items[].spec.nodeName}')
externalIP=$(kubectl get node ${nodeName} -o jsonpath='{.status.addresses[?(@.type=="ExternalIP")].address}')

export netifi_proteus_broker_hostname="${externalIP}"
export PROTEUS_SETUP_ACCESS_KEY="9007199254740991"
export PROTEUS_SETUP_ACCESS_TOKEN="kTBDVtfRBO4tHOnZzSyY5ym2kfY="
export PROTEUS_SETUP_PORT="8001"
export PROTEUS_SETUP_KEEPALIVE="false"
export PROTEUS_SETUP_SSL_DISABLED="true"

envsubst '$PROTEUS_SETUP_ACCESS_KEY $PROTEUS_SETUP_ACCESS_TOKEN $netifi_proteus_broker_hostname $PROTEUS_SETUP_PORT $PROTEUS_SETUP_KEEPALIVE $PROTEUS_SETUP_SSL_DISABLED' < ./apache-jmeter-5.0/bin/testplan.template.jmx > ./apache-jmeter-5.0/bin/testplan.jmx

