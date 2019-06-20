#!/bin/bash
[[ -f snowx.jar ]] || (mvn package && mv target/snowx-1.0-SNAPSHOT.jar snowx.jar)
echo "Running java -jar snowx.jar $*"
java -jar snowx.jar $*