#!/usr/bin/env bash
echo "Enter the docker user name: "
read docker_user
docker build --tag=alpine-java:base --rm=true .
mvn clean verify
mvn package spring-boot:repackage
docker build --tag=$docker_user/aero.minova.cas:latest .
exit

echo Stop and remove container: docker rm -f <container id>
echo docker run --name=aero.minova.cas --publish=8084:8084 $docker_user/aero.minova.cas:latest
echo List all running containers: docker ps
echo Stop docker docker stop <container id|name>
echo docker container ls
echo docker container rm <container id|name>
echo Remove all containers: docker system prune
