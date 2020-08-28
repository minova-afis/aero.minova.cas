#!/usr/bin/env bash
# Optional Cleanup: docker rm aero.minova.cas
docker run --name=aero.minova.cas --publish=8084:8084 aero.minova.cas:latest
