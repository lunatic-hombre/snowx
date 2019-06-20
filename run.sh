#!/bin/bash
[[ -f snowx.jar ]] || mvn package
echo "Running java -jar snowx.jar $*"
java -jar snowx.jar $*