#!/usr/bin/env bash
# Optional Cleanup
	# docker stop aero.minova.cas
	# docker rm aero.minova.cas
docker run \
	--name=aero.minova.cas \
	--publish=8084:8084 \
	$docker_user/aero.minova.cas:latest
