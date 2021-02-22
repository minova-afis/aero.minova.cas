#!/usr/bin/env bash
docker save --output target/aero.minova.cas.docker.tar $docker_user/aero.minova.cas:latest
echo Import funktioniert über: docker load --input target/aero.minova.cas.docker.tar
