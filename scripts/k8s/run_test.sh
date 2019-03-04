#!/bin/bash

pushd ./apache-jmeter-5.0/bin
./jmeter.sh -n -t testplan.jmx
popd
