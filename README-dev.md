# Informationen für die Entwicklung

## Maven

Maven `warnings` und entsprechende Lösungen

```warning

```

```solution

```

### pom.xml

```warning
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:cas.dev:jar:13.8.2-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.springframework.boot:spring-boot-maven-plugin is missing. @ line 49, column 21
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:cas.service:jar:13.8.2-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.springframework.boot:spring-boot-maven-plugin is missing. @ line 377, column 21
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:cas.extension.deployment:pom:13.8.2-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.springframework.boot:spring-boot-maven-plugin is missing. @ line 36, column 21
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:cas-customer-build-project:jar:13.8.2-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.springframework.boot:spring-boot-maven-plugin is missing. @ aero.minova:cas.extension.deployment:13.8.2-SNAPSHOT, C:\devsrc\workspace-main\aero.minova.cas\cas.extension.deployment\pom.xml, line 36, column 21
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:aero.minova.service.mcas:jar:13.8.2-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.springframework.boot:spring-boot-maven-plugin is missing. @ line 156, column 21
[WARNING]
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING]
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING]
```

Spring Boot Version explizit angeben um Maven Warnings zu vermeiden!

```solution
    <build>
        ...
        <!-- Spring Boot Version explizit angeben um Maven Warnings zu vermeiden!! -->
        <pluginManagement>
    		<plugins>
	            <plugin>
	                <groupId>org.springframework.boot</groupId>
	                <artifactId>spring-boot-maven-plugin</artifactId>
	                <version>3.5.16</version>
	            </plugin>   		
    		</plugins>
	    </pluginManagement>
        ...
    </build>
```


```warning
Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build as described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
WARNING: A Java agent has been loaded dynamically (C:\devdata\maven\net\bytebuddy\byte-buddy-agent\1.17.8\byte-buddy-agent-1.17.8.jar)
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
WARNING: Dynamic loading of agents will be disallowed by default in a future release
```

```solution

```

### service\pom.xml

```warning
[WARNING]
[WARNING] Some problems were encountered while building the effective model for aero.minova:cas.service:jar:13.8.2-SNAPSHOT
[WARNING] 'dependencies.dependency.scope' for com.azure.spring:spring-cloud-azure-dependencies:pom must be one of [provided, compile, runtime, test, system] but is 'import'. @ line 342, column 20
[WARNING]
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING]
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING]
```

Dependency in `<dependencyManagement>` verschieben.

```solution
   <dependencyManagement>
        <dependencies>
            ...
            <dependency>
                <groupId>com.azure.spring</groupId>
                <artifactId>spring-cloud-azure-dependencies</artifactId>
                <version>7.4.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            ...
        </dependencies>
    </dependencyManagement>

    <dependency>
        <dependencies>
            ...
            <!-- Ausdokumentieren oder löschen!!
            <dependency>
                <groupId>com.azure.spring</groupId>
                <artifactId>spring-cloud-azure-dependencies</artifactId>
                <version>7.4.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            -->
            ...
        </dependencies>
    </dependency>

```

### resource-plugin\pom.xml

```warning
[WARNING]

Some dependencies of Maven Plugins are expected to be in provided scope.
Please make sure that dependencies listed below declared in POM
have set '<scope>provided</scope>' as well.

The following dependencies are in wrong scope:
 * org.apache.maven:maven-plugin-api:jar:3.9.6:compile
 * org.apache.maven:maven-model:jar:3.9.6:compile
 * org.apache.maven:maven-artifact:jar:3.9.6:compile
 * org.apache.maven:maven-project:jar:2.2.1:compile
 * org.apache.maven:maven-settings:jar:2.2.1:compile
 * org.apache.maven:maven-profile:jar:2.2.1:compile
 * org.apache.maven:maven-artifact-manager:jar:2.2.1:compile
 * org.apache.maven:maven-repository-metadata:jar:2.2.1:compile
 * org.apache.maven:maven-plugin-registry:jar:2.2.1:compile
```

Scpoe `provided`setzen

```solution
	<dependencies>
		<dependency>
			<groupId>org.apache.maven</groupId>
			<artifactId>maven-plugin-api</artifactId>
			<version>3.9.6</version>
			<scope>provided</scope>
		</dependency>
        ...
		<dependency>
			<groupId>org.apache.maven</groupId>
			<artifactId>maven-project</artifactId>
			<version>2.2.1</version>
			<scope>provided</scope>
		</dependency>
        ...
	<dependencies>
```



