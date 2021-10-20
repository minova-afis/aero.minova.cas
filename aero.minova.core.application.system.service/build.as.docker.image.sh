#!/usr/bin/env bash
mvn clean verify
cd ../aero.minova.core.application.system.setup
  mvn clean verify
  cp ./target/*.jar ../aero.minova.core.application.system.service/target/libs/
  cp ./target/libs/*.jar ../aero.minova.core.application.system.service/target/libs/
  cp ./libs/*.jar ../aero.minova.core.application.system.service/target/libs/
cd ../aero.minova.core.application.system.service
  docker build --tag=$docker_user/aero.minova.cas:latest .
exit

echo Stop and remove container: docker rm -f <container id>
echo docker run --name=aero.minova.cas --publish=8084:8084 $docker_user/aero.minova.cas:latest
echo List all running containers: docker ps
echo Stop docker docker stop <container id|name>
echo docker container ls
echo docker container rm <container id|name>
echo Remove all containers: docker system prune
