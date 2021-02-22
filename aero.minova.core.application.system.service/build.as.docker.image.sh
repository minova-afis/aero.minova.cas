#!/usr/bin/env bash
docker build --tag=alpine-java:base --rm=true .
mvn clean verify
mvn package spring-boot:repackage
#cp target/core.application.system-12.4.0-SNAPSHOT.jar \
#	../aero.minova.core.application.system.docker/core.application.system.jar
docker build --tag=aero.minova.cas:latest --rm=true .
echo This command also contains some helpful commands, in its source code.
exit

echo docker run --name=aero.minova.cas --publish=8084:8084 --network="host" aero.minova.cas:latest
echo List all running containers: docker ps
echo Stop docker docker stop <container id|name>
echo docker container ls
echo docker container rm <container id|name>
echo Remove all containers: docker system prune
