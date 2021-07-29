FROM openjdk11:jre11u-alpine-nightly
COPY target/aero.minova.core.application.system.service.jar /opt/aero.minova.cas/lib/
ENV SPRING_APPLICATION_JSON='{}'
ENTRYPOINT ["/opt/java/openjdk/bin/java"]
CMD ["-jar", "/opt/aero.minova.cas/lib/aero.minova.core.application.system.service.jar"]
VOLUME /var/lib/aero.minova.cas
EXPOSE 8084