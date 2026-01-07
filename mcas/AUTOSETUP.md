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
   - Throws RuntimeException on failure (prevents app from starting in broken state)
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

## Authentication Considerations

### The Chicken-and-Egg Problem

Spring Security initializes **before** `@PostConstruct` methods run. This creates a dependency issue:

- `login_dataSource=database` → Requires `xtcasUsers` table
- `xtcasUsers` table → Created by setup
- Setup → Runs in `@PostConstruct`
- `@PostConstruct` → After Security initialization

**Result:** Using `database` auth before setup causes Spring Security to fail during startup.

### Solution

For auto-setup deployments, use **admin authentication** initially:

```properties
login_dataSource=admin
ng.api.autosetup=true
```

**Admin credentials:** (from `SecurityConfig.java:136`)
- Username: `admin`
- Password: `rqgzxTf71EAx8chvchMi`

**After first boot:**
- Setup has created database tables and users
- You can switch to `login_dataSource=database`
- Use actual database users for authentication

### Alternative Approaches Considered

1. **Dynamic auth switching** - Too complex, breaks Spring's initialization model
2. **Pre-initialize database** - Defeats purpose of auto-setup
3. **Custom Security configuration** - Would require major refactoring
4. **Current approach** ✅ - Simple, explicit, works reliably

## Deployment Examples

### Docker
```bash
docker run -d \
  -e SPRING_DATASOURCE_URL="jdbc:sqlserver://db:1433;databaseName=CAS" \
  -e SPRING_DATASOURCE_USERNAME="sa" \
  -e SPRING_DATASOURCE_PASSWORD="password" \
  -e NG_API_AUTOSETUP="true" \
  -e LOGIN_DATASOURCE="admin" \
  ghcr.io/minova-afis/aero.minova.mcas:latest
```

### Kubernetes
```yaml
env:
  - name: ng_api_autosetup
    value: "true"
  - name: login_dataSource
    value: "admin"
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
  --login_dataSource=admin \
  --spring.datasource.url="jdbc:sqlserver://..."
```

## Testing

### Manual Testing

1. **Deploy µCAS with empty database:**
   ```bash
   docker run -e NG_API_AUTOSETUP=true -e LOGIN_DATASOURCE=admin ...
   ```

2. **Check logs for setup messages:**
   ```
   Auto-setup service enabled - checking if database setup is required...
   Database setup required - executing setup procedure
   Starting automatic database setup...
   Automatic database setup completed successfully
   ```

3. **Verify tables created:**
   ```sql
   SELECT * FROM xtcasUsers
   SELECT * FROM xtcasAuthorities
   ```

### Automated Testing

Unit tests should verify:
- `isSetupNeeded()` correctly detects missing tables
- `isSetupNeeded()` correctly detects empty tables
- `executeSetup()` is called when needed
- `executeSetup()` is NOT called when tables exist
- Error handling works correctly

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

1. **Auto-migrate to database auth:** After setup, automatically switch from admin to database auth
2. **Health check integration:** Report setup status in `/actuator/health`
3. **Setup progress tracking:** WebSocket updates during setup execution
4. **Pre-flight checks:** Validate database compatibility before setup
5. **Rollback capability:** Undo setup if it fails partway through

## Related Issues

- [#1441 - Automatic repair & recovery](https://github.com/minova-afis/aero.minova.cas/issues/1441)
- Previous discussion about eliminating manual setup steps
