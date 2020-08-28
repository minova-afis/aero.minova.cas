#!/usr/bin/env bash
docker save --output target/aero.minova.cas.docker.tar aero.minova.cas:latest
echo Import funktioniert über: docker load --input aero.minova.cas.docker.tar
