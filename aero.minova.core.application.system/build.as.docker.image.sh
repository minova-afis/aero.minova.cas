#!/usr/bin/env bash
docker build --tag=alpine-java:base --rm=true .
mvn clean verify
mvn package spring-boot:repackage
#cp target/core.application.system-12.4.0-SNAPSHOT.jar \
#	../aero.minova.core.application.system.docker/core.application.system.jar
docker build --file=Dockerfile.server \
	--tag=aero.minova.cas:latest --rm=true .
