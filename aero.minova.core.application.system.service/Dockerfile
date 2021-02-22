FROM alpine-java:base
MAINTAINER baeldung.com
COPY target/aero.minova.core.application.system.service.jar /opt/aero.minova.cas/lib/
ENV SPRING_APPLICATION_JSON='{}'
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/aero.minova.cas/lib/aero.minova.core.application.system.service.jar"]
VOLUME /var/lib/aero.minova.cas
EXPOSE 8084