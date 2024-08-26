#!/bin/bash
nohup mvn spring-boot:run &
echo $! > ./pid.file