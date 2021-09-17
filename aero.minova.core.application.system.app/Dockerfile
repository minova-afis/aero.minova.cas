ARG casVersion=latest
FROM minova/aero.minova.cas:$casVersion
COPY target/classes /opt/aero.minova.cas/system-files/
ENTRYPOINT ["/opt/java/openjdk/bin/java"]
WORKDIR '/opt/aero.minova.cas'
CMD ["-cp", "/opt/aero.minova.cas/lib/*", "aero.minova.core.application.system.CoreApplicationSystemApplication"]
VOLUME /var/lib/aero.minova.cas
EXPOSE 8084