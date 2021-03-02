#!/usr/bin/env bash
docker login
docker push $docker_user/aero.minova.cas:latest
