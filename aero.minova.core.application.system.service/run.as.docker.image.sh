#!/usr/bin/env bash
# Optional Cleanup
	# docker stop aero.minova.cas
	# docker rm aero.minova.cas
docker run \
	--name=aero.minova.cas \
	--publish=8084:8084 \
	--env 'aero.minova.database.url=jdbc:sqlserver://host.docker.internal;databaseName=AFIS_HAM' \
	aero.minova.cas:latest
