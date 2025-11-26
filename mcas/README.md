# µCAS - Micro Core Application System

µCAS (Micro CAS) is a standalone, lightweight version of the Minova Core Application System (CAS) that operates autonomously without requiring customer-specific configuration files or infrastructure.

## What Makes µCAS Different

### Traditional CAS Deployment
- Requires customer-specific setup files and infrastructure
- Needs file system access for forms, configurations, and resources
- Complex deployment with multiple dependencies
- Customer-specific build and deployment processes

### µCAS Innovation
- **Database-Driven**: All resources (forms, configurations, i18n files) are fetched directly from the database using the NextGen API
- **Zero Configuration**: Works out-of-the-box with minimal setup
- **Fully Standalone**: Single artifact deployment with embedded resources
- **Self-Contained**: No external file dependencies beyond database connection

## Key Features

- ✅ **NextGen Database File Access**: Resources loaded from database instead of file system
- ✅ **Fat-JAR Ready**: Single executable JAR with all dependencies included
- ✅ **Container Native**: Optimized for Docker and Kubernetes deployments  
- ✅ **Database Agnostic**: Supports SQL Server, PostgreSQL, MySQL, H2
- ✅ **Production Ready**: Built-in monitoring, security, and error handling
- ✅ **Backward Compatible**: Compatible with existing CAS workflows and APIs

## Release Artifacts

### Fat-JAR Distribution
- **Artifact**: `aero.minova.service.mcas.jar` (~98MB)
- **Use Case**: On-premise deployments, development, testing
- **Deployment**: Simple `java -jar` execution
- **Repository**: [GitHub Packages](https://maven.pkg.github.com/minova-afis/aero.minova.maven.root)

### Docker Container
- **Image**: `aero.minova.mcas:latest`
- **Use Case**: Docker environments, Kubernetes, cloud deployments
- **Registry**: GitHub Container Registry
- **Platforms**: linux/amd64

## Quick Start

### Prerequisites
- Java 17+ (recommended: Java 21)
- SQL Server, PostgreSQL, or other supported database
- Database with CAS schema initialized

### Fat-JAR Deployment

1. **Download the latest release**:
   ```bash
   # Download from GitHub Packages (requires authentication)
   mvn dependency:copy -Dartifact=aero.minova:service.mcas:LATEST:jar -DoutputDirectory=.
   ```

2. **Configure database connection** (create `application.properties`):
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=YOUR_DB;encrypt=true;trustServerCertificate=true
   spring.datasource.username=YOUR_USER
   spring.datasource.password=YOUR_PASSWORD
   ```

3. **Run µCAS**:
   ```bash
   java -jar aero.minova.service.mcas.jar
   ```

4. **Access the application**:
   - Main Application: http://localhost:8084/cas
   - Management Endpoints: http://localhost:8091/actuator

### Docker Deployment

1. **Run with Docker**:
   ```bash
   docker run -d \
     -p 8084:8084 \
     -e SPRING_DATASOURCE_URL="jdbc:sqlserver://host.docker.internal:1433;databaseName=YOUR_DB;encrypt=true;trustServerCertificate=true" \
     -e SPRING_DATASOURCE_USERNAME="YOUR_USER" \
     -e SPRING_DATASOURCE_PASSWORD="YOUR_PASSWORD" \
     ghcr.io/minova-afis/aero.minova.mcas:latest
   ```

2. **Using Docker Compose**:
   ```yaml
   version: '3.8'
   services:
     mcas:
       image: ghcr.io/minova-afis/aero.minova.mcas:latest
       ports:
         - "8084:8084"
         - "8091:8091"
       environment:
         SPRING_DATASOURCE_URL: "jdbc:sqlserver://sqlserver:1433;databaseName=CAS;encrypt=true;trustServerCertificate=true"
         SPRING_DATASOURCE_USERNAME: "sa"
         SPRING_DATASOURCE_PASSWORD: "YourPassword"
       depends_on:
         - sqlserver
   ```

### Kubernetes Deployment

**Simple Example** (for testing):

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mcas
spec:
  replicas: 3
  selector:
    matchLabels:
      app: mcas
  template:
    metadata:
      labels:
        app: mcas
    spec:
      containers:
      - name: mcas
        image: ghcr.io/minova-afis/aero.minova.mcas:latest
        ports:
        - containerPort: 8084
        - containerPort: 8091
        env:
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: database-secret
              key: url
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: database-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: database-secret
              key: password
        readinessProbe:
          httpGet:
            path: /cas/actuator/health
            port: 8091
          initialDelaySeconds: 30
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /cas/actuator/health
            port: 8091
          initialDelaySeconds: 60
          periodSeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  name: mcas-service
spec:
  selector:
    app: mcas
  ports:
  - name: http
    port: 80
    targetPort: 8084
  - name: management
    port: 8091
    targetPort: 8091
```

**Production Deployment** (Kustomize + ArgoCD):

For production customer deployments using GitOps, see **[DEPLOYMENT.md](./DEPLOYMENT.md)** for comprehensive deployment documentation including:
- Kustomize-based configuration management
- ArgoCD GitOps workflows
- Environment-specific overlays (development/production)
- Multi-location deployments
- Monitoring and troubleshooting guides

**Current Deployments:**
- **SkyTanking Berlin (BER)** - Development environment serving as backend for next-gen web interface

## Configuration

µCAS uses Spring Boot's configuration system. Key configuration options:

### Database Configuration (Required)
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=CAS;encrypt=true;trustServerCertificate=true
spring.datasource.username=cas_user
spring.datasource.password=secure_password
```

### Authentication Methods
```properties
# Admin authentication (development/testing)
login_dataSource=admin

# Database authentication (production)
login_dataSource=database

# LDAP authentication (enterprise)
login_dataSource=ldap
```

### NextGen API Settings (Pre-configured)
```properties
ng.api.preferdbfiles=true
ng.api.dbfiles=true
ng.api.dbregistry=true
ng.api.mdimix=true
```

## Monitoring and Health Checks

µCAS includes built-in monitoring endpoints:

- **Health Check**: `/actuator/health`
- **Metrics**: `/actuator/prometheus` (for Prometheus integration)
- **Info**: `/actuator/info`

## Building from Source

```bash
# Clone the repository
git clone https://github.com/minova-afis/aero.minova.cas.git
cd aero.minova.cas

# Build all modules
mvn clean install -DskipTests

# Build µCAS fat-jar
mvn clean package -pl mcas -DskipTests

# Build Docker image
mvn compile jib:dockerBuild -pl mcas
```

## Architecture

µCAS is built on:
- **Spring Boot 3.2.4** - Modern Java framework with embedded Tomcat
- **NextGen API** - Database-first resource loading
- **Hibernate/JPA** - Database abstraction and ORM
- **Spring Security** - Authentication and authorization
- **Micrometer** - Metrics and monitoring

## Use Cases

- **Development Environment**: Quick local CAS instance for development
- **Microservices Architecture**: Standalone CAS service in containerized environments  
- **Cloud Deployment**: Scalable CAS instances in Kubernetes clusters
- **On-Premise Installation**: Simple single-JAR deployment for customers
- **CI/CD Testing**: Lightweight CAS instance for automated testing

## Troubleshooting

### Common Issues

1. **Database Connection Fails**
   - Verify database URL, username, and password
   - Ensure database server is accessible
   - Check firewall and network connectivity

2. **Privilege Errors**
   - Run the setup procedure: `POST /cas/procedure/setup`
   - Ensure user has appropriate database permissions
   - Verify CAS schema is properly initialized

3. **Out of Memory**
   - Increase JVM heap size: `java -Xmx2g -jar aero.minova.service.mcas.jar`
   - Consider container memory limits in Docker/K8s

### Logs and Debugging

Enable detailed logging:
```properties
logging.level.aero.minova.cas=DEBUG
spring.jpa.show-sql=true
```

## Contributing

µCAS is part of the larger CAS ecosystem. See the main [CAS repository](https://github.com/minova-afis/aero.minova.cas) for contribution guidelines.

## License

Licensed under the same terms as the main CAS project.

---

**µCAS** - Making CAS deployment simple, scalable, and standalone.