# Auto-Setup Feature

## Overview

The auto-setup feature eliminates manual database initialization by automatically running the setup procedure on first boot. This makes µCAS truly "zero-touch" for containerized deployments.

## Implementation Details

### Core Component

**File:** `service/src/main/java/aero/minova/cas/service/AutoSetupService.java`

- **Pattern:** Spring `@PostConstruct` initialization (similar to `SelfProbingService`)
- **Activation:** Only when `ng.api.autosetup=true` (via `@ConditionalOnProperty`)
- **Execution Order:** After Spring context initialization, before application ready
- **Backward Compatible:** Defaults to `false`, doesn't affect existing CAS deployments

### How It Works

1. **Detection Phase:**
   - Checks if `xtcasUsers` table exists in database
   - If table exists, checks if it has any users
   - If either check fails, setup is needed

2. **Execution Phase:**
   - Creates a setup Table with name "setup"
   - Calls `SqlProcedureController.executeProcedure()` - same as manual `/setup` endpoint
   - Ensures identical behavior whether manual or automatic

3. **Error Handling:**
   - Logs detailed information at each step
   - **Does NOT prevent application startup on failure** (avoids boot-loops in cloud)
   - Logs prominent error messages for easy diagnosis
   - Exposes setup status via `isSetupSucceeded()` for health checks
   - Can be forced with `ng.api.autosetup.force=true` for dev/testing

### Configuration

**For µCAS (enabled by default):**
```properties
ng.api.autosetup=true                # Enable auto-setup
ng.api.autosetup.force=false         # Force setup even if tables exist
```

**For traditional CAS (disabled by default):**
```properties
# Auto-setup is not enabled - backward compatible
# Manually enable if desired:
ng.api.autosetup=true
```

## Authentication & Security

### Temporary Security Context

Auto-setup creates a **temporary security context** with elevated privileges:

- **User:** `CAS_AUTOSETUP`
- **Authority:** `ROLE_ADMIN`
- **Lifetime:** Only during setup execution
- **Cleanup:** Always cleared in finally block (even on errors)

This approach:
- ✅ Bypasses privilege checks during setup
- ✅ No `login_dataSource=admin` required
- ✅ Works with any authentication method (database, LDAP, admin)
- ✅ Secure - temporary context is immediately cleared

### Default Admin User

Auto-setup automatically creates a default admin user for initial login:

**Default Credentials:**
- **Username:** `admin`
- **Password:** `rqgzxTf71EAx8chvchMi`
- **Authority:** `admin` (full privileges)

**Important Notes:**
- ⚠️ **Change this password after first login!**
- User is created only if it doesn't already exist (idempotent)
- Same password as in-memory admin (from `SecurityConfig.java`)
- Eliminates need for manual user creation via SQL

### Recommended Authentication Configuration

**For µCAS deployments (recommended):**
```properties
login_dataSource=database    # Use database authentication
ng.api.autosetup=true        # Enable auto-setup
```

**After auto-setup completes:**
1. Login with default credentials (`admin` / `rqgzxTf71EAx8chvchMi`)
2. Change admin password immediately
3. Create additional users as needed
4. System is ready for production use

**No manual SQL required!** The days of `login_dataSource=admin` workarounds are over.

## Deployment Examples

### Docker
```bash
docker run -d \
  -e SPRING_DATASOURCE_URL="jdbc:sqlserver://db:1433;databaseName=CAS" \
  -e SPRING_DATASOURCE_USERNAME="sa" \
  -e SPRING_DATASOURCE_PASSWORD="password" \
  -e NG_API_AUTOSETUP="true" \
  -e LOGIN_DATASOURCE="database" \
  ghcr.io/minova-afis/aero.minova.mcas:latest
```

**After first boot:**
- Login: `admin` / `rqgzxTf71EAx8chvchMi`
- Change password immediately
- Ready for production

### Kubernetes
```yaml
env:
  - name: ng_api_autosetup
    value: "true"
  - name: login_dataSource
    value: "database"
  - name: spring_datasource_url
    valueFrom:
      secretKeyRef:
        name: database-secret
        key: url
```

### Fat-JAR
```bash
java -jar aero.minova.service.mcas.jar \
  --ng.api.autosetup=true \
  --login_dataSource=database \
  --spring.datasource.url="jdbc:sqlserver://..."
```

## Testing

### Manual Testing

1. **Deploy µCAS with empty database:**
   ```bash
   docker run -e NG_API_AUTOSETUP=true -e LOGIN_DATASOURCE=database ...
   ```

2. **Check logs for setup messages:**
   ```
   Auto-setup service enabled - checking if database setup is required...
   Database setup required - executing setup procedure
   Starting automatic database setup...
   Created default admin user - Username: admin, Password: rqgzxTf71EAx8chvchMi
   IMPORTANT: Default admin user created. Please change password after first login!
   Automatic database setup completed successfully
   Auto-setup security context cleared
   ```

3. **Verify tables and users created:**
   ```sql
   SELECT * FROM xtcasUsers WHERE Username = 'admin'
   SELECT * FROM xtcasAuthorities WHERE Username = 'admin'
   SELECT * FROM xtcasUserGroup WHERE KeyText = 'admin'
   ```

4. **Test login:**
   - Navigate to `http://localhost:8084/cas`
   - Login with `admin` / `rqgzxTf71EAx8chvchMi`
   - Change password immediately

### Automated Testing

Unit tests should verify:
- `isSetupNeeded()` correctly detects missing tables
- `isSetupNeeded()` correctly detects empty tables
- `executeSetup()` is called when needed
- `executeSetup()` is NOT called when tables exist
- Error handling works correctly
- Application starts even when setup fails

## Troubleshooting Failed Auto-Setup

### Symptoms

If auto-setup fails, you'll see a prominent error box in logs:

```
╔════════════════════════════════════════════════════════════════════╗
║                     AUTO-SETUP FAILED!                             ║
╠════════════════════════════════════════════════════════════════════╣
║  Database initialization failed but application will continue.    ║
║  The application may NOT function correctly!                       ║
...
```

**Important:** The application **will start** but may not function correctly.

### Diagnosis Steps

1. **Check database connectivity:**
   ```bash
   kubectl logs <pod-name> | grep -i "database\|connection"
   ```

2. **Verify database permissions:**
   - User must have CREATE TABLE, CREATE VIEW, CREATE PROCEDURE
   - Check if schema/user has sufficient privileges

3. **Check setup status programmatically:**
   ```java
   @Autowired AutoSetupService autoSetupService;

   if (!autoSetupService.isSetupSucceeded()) {
       Exception e = autoSetupService.getSetupException();
       // Handle setup failure
   }
   ```

4. **Manual setup fallback:**
   ```bash
   # Access the running pod
   kubectl exec -it <pod-name> -- /bin/sh

   # Call setup endpoint manually
   curl -X POST http://localhost:8084/cas/data/procedure \
     -H "Content-Type: application/json" \
     -d '{"name":"setup","columns":[],"rows":[]}'
   ```

### Common Causes

| Error | Cause | Solution |
|-------|-------|----------|
| Connection timeout | Database not reachable | Check network/firewall rules |
| Permission denied | Insufficient DB privileges | Grant CREATE permissions to DB user |
| Constraint violations | Partial existing schema | Drop existing tables or use `force=true` |
| ANSI_WARNINGS error | Filtered index issue | Check if Hibernate constraints exist |
| Table already exists | Concurrent setup attempts | Normal - first pod wins, others skip |

### Prevention

**Use health checks** to detect setup failures:
```yaml
livenessProbe:
  httpGet:
    path: /actuator/health
    port: 8084
  initialDelaySeconds: 60

readinessProbe:
  httpGet:
    path: /actuator/health
    port: 8084
  periodSeconds: 10
```

## Backward Compatibility

✅ **100% Backward Compatible**

- **Traditional CAS:** Property defaults to `false`, no behavior change
- **Existing µCAS deployments:** Must explicitly enable to use feature
- **Manual setup:** Still works via `POST /setup` endpoint
- **Extensions:** No impact on existing extensions or bootstrap checks

## Benefits

### For Development
- **Faster iteration:** No manual setup between database resets
- **CI/CD friendly:** Automated testing with fresh databases
- **Developer experience:** Clone, run, done

### For Operations
- **Zero-touch deployment:** Container starts and self-initializes
- **Idempotent:** Safe to redeploy, won't re-run if already set up
- **GitOps ready:** Configuration as code, no manual steps
- **Scalability:** Multiple replicas can start simultaneously (first wins, others skip)

### For Customers
- **Simplified installation:** Remove manual steps from documentation
- **Reduced errors:** No forgotten setup steps
- **Better onboarding:** Customer can test immediately

## Future Enhancements

Potential improvements:

1. **Configurable admin password:** Allow setting via environment variable `MCAS_ADMIN_PASSWORD`
2. **Health check integration:** Report setup status in `/actuator/health`
3. **Setup progress tracking:** WebSocket updates during setup execution
4. **Pre-flight checks:** Validate database compatibility before setup
5. **Rollback capability:** Undo setup if it fails partway through
6. **Password change enforcement:** Require password change on first login

## Related Issues

- [#1441 - Automatic repair & recovery](https://github.com/minova-afis/aero.minova.cas/issues/1441)
- Previous discussion about eliminating manual setup steps
