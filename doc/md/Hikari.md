# HikariCP Configuration Analysis for CAS

## Original Comment by AVM

> **Author:** AVM
> **Context:** application.properties discussion about maximumPoolSize
>
> spring.datasource.hikari.maximumPoolSize wird nicht gesetzt:
>
> Mit einer sehr großen Poolgröße wollte man, dass bei Verbindungsproblemen neue funktionierende Verbindungen schneller aufgebaut werden:
> Timeouts führen dazu, dass neue Verbindungen deutlich länger aktiv in Verwendung sind als notwendig,
> wodurch neue REST-Anfragen dazu neigen im Pool neue Verbindungen zu erzeugen.
> Die erhöhte Poolgröße verhindert, dass neue Anfragen auf einem neuen Slot aufgrund der toten Verbindungen warten.
>
> Diese Taktik ist aus folgenden Gründen schlecht:
> * Eine zu große Poolsize führt dazu, dass potentiel mehr Verbindungen aktiv sind und dadurch zu viele Ports blockiert werden.
>   In TTA hat es sämtliche neue Netzwerkverbindungen blockiert: https://github.com/minova-afis/com.minova.oiltanking.twb/issues/2052
> * Eine zu große Poolsize ist einfach paranoid. Wenn das SQL nicht geht, könnte man stattdessen im schlimmsten Fall auch einfach den Dienst neu starten.
>   Siehe SelfProbingService.
> * AVM: ich habe die Vermutung das HikariCP generell schlecht mit Sonderfällen von Verbindungsabbrüchen arbeitet,
>   was vielleicht auch an den JDBC-Treibern selber liegt.
>   Der keepalive Kommentar in der README von HikariCP vom 2024 scheint auch ein Hinweis darauf zu sein.
>   So, gab es mal vor Jahren einen Fall bei uns (nicht CAS, sondern ein einfacher Spring Boot/Hibernate Dienst),
>   wo der HikariCP-Pool mit kaputten Verbindungen vollgelaufen ist und dann blockiert war,
>   obwohl die DB einige Sekunden/Minuten später wieder erreichbar war.
>   Solche Probleme kann man wahrscheinlich gar nicht wirklich kompensieren, wenn der JDBC-Treiber nicht mitspielt oder unglücklich konfiguriert ist.
>   Wenn der JDBC-Treiber von unserem Java-Code falsch verwendet wird, dann wäre es auch ein Problem, das HikariCP nicht kompensieren kann.
>   Würde zumindest meine HikariCP-Paranoia erklären.
>   Auch scheint in HikariCP die Testabdeckung für Fehlerfälle generell niedriger zu sein, als beim restlichen Code: https://app.codecov.io/gh/brettwooldridge/HikariCP/blob/dev/src%2Fmain%2Fjava%2Fcom%2Fzaxxer%2Fhikari%2Fpool%2FProxyConnection.java

## Why Configuration Values Were Changed

### 1. `leakDetectionThreshold` (300000ms = 5 minutes)

**Original Reasoning:**
- Comment stated: "corresponds to longest procedure time (especially daily closing which **doesn't exist yet** for CAS)"
- Assumption was 5 minutes sufficient for all procedures

**Why Changed:**
- **Reality**: Long-running procedures (e.g., daily closing) **do exist** and can take up to **15 minutes**
- Current value causes false-positive leak warnings during legitimate long operations
- **Recommendation**: Increase to **20 minutes (1200000ms)** to accommodate longest procedures with buffer

### 2. `maxLifetime` (600000ms = 10 minutes)

**Original Reasoning:**
- Comment said: "Connection max 10 minutes in pool without being used, then closed"
- **This is incorrect** - that's `idleTimeout`, not `maxLifetime`

**Why Changed:**
- `maxLifetime` = **total lifetime from creation**, even if **actively used**
- HikariCP will **forcibly close** a connection after 10 minutes, **even mid-transaction**
- A 15-minute procedure would be **killed at the 10-minute mark**
- **Critical Issue**: This causes unexplained transaction failures during long operations
- **Recommendation**: Increase to **30 minutes (1800000ms)** or **1 hour (3600000ms)**

### 3. `maximumPoolSize` (not set, default = 10)

**Original Reasoning (kept):**
- Detailed historical context about TTA port exhaustion issue (#2052)
- Valid concerns about JDBC driver edge cases and HikariCP's handling of connection failures
- Decision to keep default (10) to avoid resource exhaustion

**No Change Needed:**
- AVM's reasoning remains valid
- Default pool size (10) is conservative and safe
- Only increase if pool starvation becomes frequent issue

### 4. `keepaliveTime` (30000ms = 30 seconds)

**Original Reasoning:**
- Must be less than `maxLifetime` to function
- Prevents database from closing idle connections

**No Change Needed:**
- Value is appropriate
- Works well with SQL Server connection timeouts
- Keeps connections alive during idle periods

## What Happens When TCP Connection to SQL Server is Lost

### Scenario: Network Partition or SQL Server Restart

#### 1. **Initial State**
- HikariCP pool has N connections (e.g., 10)
- All connections appear healthy in pool
- SQL Server/Azure SQL becomes unreachable (network issue, restart, failover)

#### 2. **Application Requests Connection**
- Application calls `systemDatabase.getConnection()`
- HikariCP returns a connection from pool
- **Connection appears valid** but TCP socket is broken

#### 3. **First SQL Operation Attempt**
- Application executes query/procedure
- JDBC driver attempts to send data over broken TCP connection
- **Exception thrown**: `SQLException: "Connection reset"` or `"Broken pipe"` or timeout

#### 4. **HikariCP Detection**
- HikariCP marks connection as invalid when SQLException occurs
- Connection is **evicted from pool**
- HikariCP attempts to create **new connection**

#### 5. **Pool Recovery Behavior**

**If SQL Server is back online:**
- New connection succeeds
- Application retries operation (or fails if no retry logic)
- Pool gradually replaces broken connections as they're detected

**If SQL Server still unreachable:**
- New connection attempt **fails**
- HikariCP retries with exponential backoff
- Other threads requesting connections will **block** until:
  - `connectionTimeout` expires (default 30s) → throws `SQLTransientConnectionException`
  - OR new connection succeeds

#### 6. **The Problem: "Pool Filled with Dead Connections"**

This is the scenario AVM mentioned:

```
Before failure:  [C1✓] [C2✓] [C3✓] [C4✓] [C5✓] [C6✓] [C7✓] [C8✓] [C9✓] [C10✓]
Network fails:   [C1✗] [C2✗] [C3✗] [C4✗] [C5✗] [C6✗] [C7✗] [C8✗] [C9✗] [C10✗]

Request 1: Gets C1✗ → tries SQL → fails → C1 evicted → tries new connection → network still down → blocks
Request 2: Gets C2✗ → tries SQL → fails → C2 evicted → tries new connection → network still down → blocks
...
Request 10: Gets C10✗ → tries SQL → fails → C10 evicted → tries new connection → network still down → blocks

Result: All pool slots blocked trying to create new connections, but network is still down
        New requests wait for connectionTimeout (30s) before failing
```

**Why `keepaliveTime` helps:**
- Periodically tests **idle** connections with `SELECT 1` or similar
- Detects broken connections **before** they're handed to application
- But only works for **idle** connections, not ones actively in use

**Why this is hard to solve:**
- JDBC drivers vary in how they detect connection failures
- Some drivers have long TCP timeouts before detecting breaks
- SQL Server driver may not immediately detect network partition
- HikariCP relies on driver cooperation

#### 7. **CAS Mitigation: SelfProbingService**

CAS has `SelfProbingService` which:
- Periodically executes `SELECT 1` to test database health
- If database unreachable, **shuts down the service** (`System.exit()`)
- Forces restart/health check rather than serving errors
- Aligns with AVM's philosophy: "if SQL doesn't work, just restart the service"

### Azure SQL Specific Considerations

**Idle Connection Killing:**
- Azure SQL kills connections idle for **30 minutes**
- HikariCP's `keepaliveTime=30s` prevents this (keeps connections active)

**Failover Scenarios:**
- Azure SQL can failover to replica during maintenance
- Causes brief connection interruption (10-30 seconds)
- Applications should have retry logic for `SQLTransientConnectionException`

**Connection Limits:**
- Azure SQL has connection limits per tier (e.g., 100 for Basic, 200 for S3)
- Multiple CAS instances share this limit
- Keep `maximumPoolSize` conservative (default 10 is good)

## Recommended Configuration for CAS

### Final Properties

```properties
# HikariCP Configuration - optimized for CAS workload
# Context: Long-running procedures (up to 15min), MS SQL Server / Azure SQL

# Keep default pool size to avoid port exhaustion (TTA #2052)
spring.datasource.hikari.maximumPoolSize=10

# Test idle connections every 30s to detect failures early
spring.datasource.hikari.keepaliveTime=30000

# Allow 20min before warning about connection leaks (longest procedure: 15min daily closing)
spring.datasource.hikari.leakDetectionThreshold=1200000

# Connection lifetime: 1 hour (must be > longest procedure duration)
spring.datasource.hikari.maxLifetime=3600000

# Connection acquisition timeout: 30s (reasonable for most cases)
spring.datasource.hikari.connectionTimeout=30000
```

### Why These Values Are Good for CAS

**`maximumPoolSize=10` (default)**
- Conservative: Prevents port exhaustion on Windows (65k port limit)
- Historical evidence: Large pools caused TTA production issues
- Sufficient for typical load: 10 concurrent long operations is already high
- Aligns with AVM's reasoning: avoid paranoid over-provisioning

**`keepaliveTime=30000` (30 seconds)**
- Keeps connections alive during idle periods
- Prevents Azure SQL 30-minute idle timeout
- Detects broken connections before they're used
- Low overhead: lightweight `SELECT 1` queries

**`leakDetectionThreshold=1200000` (20 minutes)**
- Accommodates longest known procedure (15 minutes daily closing) with 5-minute buffer
- Prevents false-positive leak warnings during legitimate operations
- Still detects real leaks (unclosed connections held for 20+ minutes)

**`maxLifetime=3600000` (1 hour)**
- **Critical**: Must be longer than longest procedure (15 minutes)
- Prevents HikariCP from killing active transactions mid-execution
- Balances connection freshness with operational requirements
- Old enough to avoid Azure SQL stale connection issues

**`connectionTimeout=30000` (30 seconds, default)**
- Reasonable wait time when pool is exhausted
- Fails fast rather than hanging indefinitely
- Works with SelfProbingService strategy (fail and restart rather than serve errors)

### What We Intentionally Don't Set

**`minimumIdle`** - Defaults to equal `maximumPoolSize` (10)
- Pool doesn't shrink, maintaining consistent performance
- Simpler behavior: all connections always available

**`idleTimeout`** - Not relevant when `minimumIdle = maximumPoolSize`
- Pool never shrinks below maximumPoolSize
- Setting would have no effect

**`validationTimeout`** - Default 5 seconds is fine
- Health checks (`SELECT 1`) complete in milliseconds
- No need to change

**`autoCommit`** - Set programmatically in code to `false`
- CAS uses explicit transaction control (manual commit/rollback)
- Cannot be overridden via properties

### Monitoring Recommendations

**Watch for these in logs:**

1. **"Connection leak detected"** warnings
   - If appearing during daily closing: increase `leakDetectionThreshold` further
   - If appearing during normal operations: investigate code for unclosed connections

2. **"Failed to obtain JDBC Connection"** errors
   - Pool exhaustion: consider increasing `maximumPoolSize` cautiously (15-20)
   - Database unreachable: SelfProbingService should trigger shutdown

3. **"Connection closed"** errors during long procedures
   - `maxLifetime` too short: increase to 2 hours (7200000ms)

4. **Frequent connection creation in logs**
   - May indicate broken connections not detected by `keepaliveTime`
   - Check database server logs for connection resets

### Testing Long Procedures

When deploying changes:
1. Test daily closing procedure (15-minute case) end-to-end
2. Verify no "connection closed" errors mid-procedure
3. Verify no false leak warnings in logs
4. Monitor connection pool metrics during procedure execution
