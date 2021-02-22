#!/usr/bin/env bash
./build.as.docker.image.sh
docker login
echo "Enter the docker user name for release: "
read docker_user
docker push $docker_user/aero.minova.cas:latest
