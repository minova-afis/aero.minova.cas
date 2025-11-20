# Claude User Inputs

## First start - Claude briefing

CAS is a service with multi-module maven structure. There are workflows to release single artifacts for every module. CAS by itself was formerly not usable as-is,
i.e. a separate repository was required to pack further customer-related files (sql scripts, XML UI descriptions, settings, etc) along with cas artifacts into a fat-jar.
My current attempt is to create a "stand-alone" version of cas. Internally I call it µCAS, to signalize that the CAS service is "livable" without further customer files
-- instead the files are fetched from the database via the ch.minova.foundation.db.rest library (taht offers Java-Methods to access application files from the database via jpa).
In other words: CAS became an autonomous mode by using the new library, such that a standard CAS service can be installed on any existing customer system (on-premise or cloud) and
pointed to another database (application.properties), from which CAS accesses the files that are requested via files/read endpoints.

Locally I do the following to test CAS in the new mode:
- clone aero.minova.cas repo (to which you now have access)
- modify service/application.properties to enable database-file access and to point CAS to a local database
- run Debug mode in Eclipse IDE within the /customer-build-project with main clas being aero.minova.cas.CoreApplicationSystemApplication

After that CAS is available locally via 8084 port, that I can access with our Web-Application-UI to request all customer-specific UI files (XML, translations) and to perform DB actions

What I want to do now: create two workflows that release a fat-jar (artifact name ch.minova.service.mcas) for on-premise installations and a docker container (named aero.minova.mcas) for k8s-based installations.
Both artrifacts should be released in our central repo: https://github.com/minova-afis/aero.minova.maven.root.

Before, the only way to release "workable" CAS-service was to create a customer specific version of it with added customer related files. An example is in the cloned repository ../com.minova.sky.swb -- take a look at it

Now, we also want to have Standard fat-jar and container releases

To avoid using a Dockerfile, I want to use Jib-Maven-Plugin within the relevant pom, such that with same pom I can release a fat-jar artifact or release a docker container whith same fat-jar as content and some Standard preferences:

- incoming ports 8084 and 8081
- aero.minova.cas.CoreApplicationSystemApplication as the main class
- additional Java VM Parameters: "-Djdk.tls.client.protocols=TLSv1.2"
- further settings we are defining in our dockerfiles
```
RUN apk add --no-cache tzdata socat
ENV TZ=Europe/Berlin
ENV LC_ALL=de.UTF-8
ENV LANG=de.UTF-8
ENV LANGUAGE=de:de
```

IMPORTANT: all necessary steps must remain the repository backward-compatible to the current release workflows. So if changes are required in the pom, they should not affect existing pipelines.

Think about it and propose a solution. Do I need to define another pom-Files to remain backward compatible and still be able to fulfill my task? Would it be better to create separate Repos on Github just for
building?