#!/bin/sh

export URI=mongodb://localhost:27017
export DB_NAME=test
export PW=test
export PORT=5000

mvn exec:java -Dexec.mainClass="earth.xor.Main"
