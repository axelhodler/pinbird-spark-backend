#!/bin/sh

export URI=test
export DB_NAME=test
export PW=test

mvn exec:java -Dexec.mainClass="earth.xor.Main"
