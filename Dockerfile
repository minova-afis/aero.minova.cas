FROM eclipse-temurin:17-jre-alpine
    # Dies ist ein Java-11 Docker-Image von Adoptium.
    # Da wir generelle JDK/JREs von Adoptium bevorzugen, verwenden wir auch deren Docker-Image.
    # Hier wird die Docker-Organisation von Adoptiums-Seite aus verlinkt: https://blog.adoptium.net/2021/08/using-jlink-in-dockerfiles/
    # Hier ist die verlinkte Docker-Organisation: https://hub.docker.com/_/eclipse-temurin
    # Wir verwenden die alpine-Variante, um die Größe des Docker-Images zu minimieren.

LABEL org.opencontainers.image.source=https://github.com/minova-afis/aero.minova.cas
LABEL maintainer=service@minova.com

COPY service/target/libs /opt/aero.minova.cas/lib/
COPY service/target/*.jar /opt/aero.minova.cas/lib/
COPY setup/target/libs /opt/aero.minova.cas/lib/
COPY setup/target/*.jar /opt/aero.minova.cas/lib/
COPY app/target/classes /opt/aero.minova.cas/system-files/
COPY app/target/classes/files /opt/aero.minova.cas/system-files/
COPY app/target/classes/forms /opt/aero.minova.cas/system-files/
ENV aero_minova_core_application_root_path='/opt/aero.minova.cas/system-files/'
ENTRYPOINT ["/opt/java/openjdk/bin/java"]
CMD ["-cp", "/opt/aero.minova.cas/lib/*", "aero.minova.cas.CoreApplicationSystemApplication"]

VOLUME /var/lib/aero.minova.cas

EXPOSE 8084
EXPOSE 8081
