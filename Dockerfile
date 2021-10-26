FROM adoptopenjdk/openjdk11:jre11u-alpine-nightly
COPY aero.minova.core.application.system.service/target/libs /opt/aero.minova.cas/lib/
COPY aero.minova.core.application.system.service/target/*.jar /opt/aero.minova.cas/lib/
COPY aero.minova.core.application.system.setup/target/libs /opt/aero.minova.cas/lib/
COPY aero.minova.core.application.system.setup/target/*.jar /opt/aero.minova.cas/lib/
COPY aero.minova.core.application.system.app/target/classes /opt/aero.minova.cas/system-files/
COPY aero.minova.core.application.system.app/target/classes/forms /opt/aero.minova.cas/system-files/

ENV login_dataSource='admin'
ENV aero_minova_database_url='jdbc:sqlserver://host.docker.internal;databaseName=AFIS_GDN'
ENV aero_minova_database_user_name='sa'
ENV aero_minova_database_user_password='Minova+0'
ENV logging_level_root=DEBUG
ENV aero_minova_core_application_root_path='/opt/aero.minova.cas/system-files/'

ENTRYPOINT ["/opt/java/openjdk/bin/java"]
CMD ["-cp", "/opt/aero.minova.cas/lib/*", "aero.minova.core.application.system.CoreApplicationSystemApplication"]
VOLUME /var/lib/aero.minova.cas
EXPOSE 8084