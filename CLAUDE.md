# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

CAS (Core Application System) is a Spring Boot-based REST API that provides a bridge between SQL databases, Java extensions, and a read-only file system. It serves as the backend infrastructure for Eclipse/WFC applications, essentially providing REST interfaces to SQL functionalities similar to legacy Ncore and Install-Tool systems.

## Architecture

This is a multi-module Maven project with the following key modules:

- **api/**: REST API definitions and domain models
- **service/**: Main Spring Boot application with controllers, services, and repositories  
- **app/**: Application configuration, database setup scripts, forms, and i18n resources
- **app.legacy/**: Legacy application support
- **client/**: OSGi client helper components
- **setup/**: Database setup and migration tools
- **resource-plugin/**: Maven plugin for resource management

## Development Commands

### Build and Test
```bash
# Build entire project
mvn clean verify

# Build specific module (from root)
mvn clean verify -pl service
mvn clean verify -pl api
mvn clean verify -pl app

# Run tests only
mvn test

# Build without tests
mvn clean compile -DskipTests
```

### Running the Application
```bash
# Run Spring Boot service (from service/ directory)
mvn spring-boot:run

# Or with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=development
```

### Docker Commands
```bash
# Build Docker image
./service/build.as.docker.image.sh

# Run as Docker container
./service/run.as.docker.image.sh

# Export Docker image
./service/export.docker.image.sh
```

### Database Setup
The application requires database initialization via the `setup` procedure:
- Access via browser: `http://localhost:8084/cas` → Login → Setup button
- Or via REST: POST to `/data/procedure` with `{"name": "setup", "columns": [], "rows": []}`

## Key Configuration Files

- **service/src/main/resources/application.properties**: Main Spring Boot configuration
- **service/lib/application-production.properties**: Production settings template
- **app/src/main/app/setup/Setup.xml**: Database setup configuration
- **service/src/main/resources/logback-spring.xml**: Logging configuration

## Spring Boot Architecture

### Main Application
- **Entry Point**: `aero.minova.cas.CoreApplicationSystemApplication`
- **Component Scanning**: Scans `aero.minova.*`, `com.minova.*`, `ch.minova.foundation.rest.*` packages
- **Database**: Supports SQL Server, PostgreSQL, MySQL via JPA/Hibernate
- **Security**: Spring Security with LDAP, database, or admin authentication

### Key Controllers
- **SqlProcedureController**: Executes stored procedures
- **XSqlProcedureController**: Extended procedure execution with error handling
- **SqlViewController**: Database view access
- **FilesController**: File system operations
- **CommunicationController**: Hub communication

### Core Services
- **ProcedureService**: Database procedure execution
- **AuthorizationService**: User rights and permissions
- **SecurityService**: Authentication and security
- **ViewService**: Database view operations (MSSQL/JOOQ implementations)
- **FilesService**: File system access
- **QueueService**: Background job processing

## Database Support

Primary: **Microsoft SQL Server**
Secondary: **PostgreSQL** (limited support - privileges and extensions only)
Also supports: **MySQL**

### Database Configuration
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=DBNAME
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

## Testing

- **Unit Tests**: Located in `src/test/java/`
- **Integration Tests**: Use `@SpringBootTest` annotation
- **Test Resources**: `src/test/resources/` contains test data and configurations

### Running Specific Tests
```bash
# Run specific test class
mvn test -Dtest=ProcedureServiceTest

# Run tests for specific module
mvn test -pl service
```

## Extension Development

CAS supports extensions via:
- **Java Extensions**: Placed in `lib/` directory, automatically loaded
- **SQL Scripts**: Database procedures and views
- **Configuration**: Via application.properties

Extensions should follow the `aero.minova.*` or `com.minova.*` package structure to be automatically discovered.

## Database Schema Management

- **Tables**: Defined in `app/src/main/app/tables/*.table.xml`
- **SQL Scripts**: Located in `app/src/main/app/sql/`
- **Setup Process**: Handled by Setup.xml configuration
- **Version Control**: Managed via `spMinovaUpdateVersion.sql`

## Logging

Structured logging with multiple appenders:
- **Console**: Standard output
- **Files**: Separate logs for different components (spring, user requests, privileges, setup)
- **Error Tracking**: Dedicated error logging
- **Custom Logger**: `aero.minova.cas.CustomLogger`

## Security Notes

- Uses Spring Security with multiple authentication providers
- Supports LDAP integration via `MultipleLdapDomainsAuthenticationProvider`
- Column-level security via `ColumnSecurityService`
- User privilege system with groups and authorities

## Development Environment Setup

1. **Database**: Set up SQL Server with appropriate database
2. **Java**: JDK 11+ required (defined in parent POM)
3. **Configuration**: Create local `application.properties` with database connection
4. **First Run**: Execute database setup procedure
5. **Extensions**: Place extension JARs in `lib/` directory if needed

## Common Issues

- **Database Connection**: Ensure SQL Server allows TCP connections
- **Setup Failures**: Check database permissions and connection string
- **Extension Loading**: Verify extensions are in correct package structure
- **Memory**: For production, consider JVM memory limits (e.g., `-Xmx256m -Xms128m`)