#!/bin/bash
kill $(cat ./pid.file)
nohup mvn spring-boot:run &
echo $! > ./pid.file