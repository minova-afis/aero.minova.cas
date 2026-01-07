# CAS Architecture Evolution & Unification Strategy

## Historical Context

### 11-styled Applications (Legacy, ~12 years old)
**Architecture:** Single-tier, on-premise
**Status:** Still in use by 99% of customer solutions

**Components:**
1. **Installation Tool (Assist)**
   - Downloads modules (JARs containing Setup.xml, no code)
   - Installs database schema from Setup.xml
   - Copies XML forms from modules to special directory for front-end

2. **Module Structure**
   - JAR artifact without application code
   - `Setup.xml` defines:
     - Dependencies (other modules)
     - Registry entries (XBS - configuration)
     - SQL procedures/scripts/tables

3. **Database**
   - Setup by Assist using SQL scripts from Setup.xml
   - Direct SQL access (stored procedures, views)

4. **Front-End**
   - RCP (Eclipse-based) application
   - **Direct database access** (no middle tier)
   - Reads XML forms from local directory
   - Executes SQL procedures directly

**Data Flow:**
```
Assist → Downloads Modules → Installs DB → Copies Forms → Local Directory
                                ↓
RCP Front-End ←──────────── Direct DB Access
```

### 12-styled Applications (First REST API Layer)
**Architecture:** Two-tier
**Status:** Transition architecture, partially adopted

**Key Change:** Introduced CAS as REST API layer

**Components:**
1. **CAS (Core Application System)**
   - REST API back-end
   - Exposes SQL procedures/views as REST endpoints
   - `/data/view` - query database views
   - `/data/procedure` - execute stored procedures
   - `/data/xprocedure` - extended procedure execution
   - XML forms still stored in file system or database

2. **Front-End**
   - Still RCP-based
   - **No longer direct DB access**
   - Calls CAS REST API instead
   - XML forms retrieved via CAS

3. **Database & Setup**
   - Same as 11: Setup.xml driven
   - SQL procedures/scripts remain almost identical
   - Assist still handles installation

**Data Flow:**
```
Assist → Downloads Modules → Installs DB
                                ↓
RCP Front-End ←── REST API ─── CAS ←── SQL ─── Database
```

**Backward Compatibility:**
- XML forms format unchanged
- SQL procedures format unchanged
- Migration path: Replace direct DB calls with CAS REST calls

### 13-styled Applications (JPA/Cloud-Native)
**Architecture:** Cloud-native, microservices
**Status:** **Only ONE customer's partial solution uses this!**

**Key Changes:**
- **No fat-jar** (cloud deployment with separate JARs)
- **JPA-based** database layer (not SQL procedures)
- **Liquibase** for schema migrations (not Setup.xml)
- Incompatible with 11/12 architecture

**Problems:**
- Required new architectural knowledge from developers
- Not compatible with existing 11/12 systems
- Can't migrate existing customers (too custom)
- CAS 13 codebase exists but building mechanism differs
- **Setup.xml mechanism partially broken** (designed for cloud, not fat-jar)

**Why It Failed:**
- Didn't support 12-er systems
- Breaking changes without migration path
- 99% of solutions still on 11
- One-off architecture for single customer

---

## Current State: Fragmented Architecture

**Reality Check:**
- **99% of customers:** 11-styled apps (direct DB or partial 12 migration)
- **<1% of customers:** One 13-styled solution (JPA/Liquibase)
- **Development team:** Needs to know 3 different architectures
- **Maintenance nightmare:** Can't just "update" everything

**Why Unification Failed Previously:**
- Customer solutions are highly customized
- No resources to reimplement everything in JPA
- Existing on-premise tables conflict with JPA annotations
- Migration path not clear

---

## Unification Strategy: MCAS (Micro CAS)

### Vision: One CAS, Multiple Setup Methods

**Goal:** Break the 11/12/13 concept → **One unified CAS (MCAS)**

**Support TWO module definition approaches:**

1. **SQL-based modules** (formerly 11/12 style)
   - Setup.xml with SQL scripts
   - Table definitions (XML)
   - Stored procedures
   - Compatible with existing customer solutions

2. **JPA-based modules** (formerly 13 style)
   - Entity annotations
   - Liquibase migrations
   - Spring Data repositories
   - For new greenfield projects

### Phase 1: MCAS + 11-styled Apps ✅ COMPLETE

**Achievement:** MCAS as back-end for 11-styled applications

**Key Innovation:**
- Assist writes files to database (`tFile` table) instead of file system
- Assist writes registry to database (`tRegistry` table) from Setup.xml XBS entries
- MCAS reads resources from database via NextGen API
  - `ng.api.preferdbfiles=true`
  - `ng.api.dbfiles=true`
  - `ng.api.dbregistry=true`
  - `ng.api.mdimix=true`

**Benefits:**
- 11-styled apps get REST API without code changes
- XML forms in database (no file system dependencies)
- Configuration in database (no registry files)
- New Web UI can access 11-styled apps via browser
- **No need to reimplement everything**

**Architecture:**
```
Assist → Modules → DB (tFile, tRegistry, SQL setup)
                        ↓
Web UI ←── REST API ─── MCAS ←── NextGen API ─── Database
                                      ↓
                                  tFile, tRegistry
```

### Phase 2: MCAS + 12-styled Apps 🔄 IN PROGRESS

**Current Challenge:** MCAS (derived from CAS 13) needs to support Setup.xml properly

**Problems Discovered:**
1. CAS 13 designed for cloud (separate JAR files), not fat-jar
2. Resource discovery expects dependencies as separate files in `jar-dependencies/`
3. cas.app's Setup.xml/SQL/tables trapped in nested JAR (`BOOT-INF/lib/`)
4. `SetupService` can't find dependency Setup.xml files in fat-jar structure

**What Works:**
- cas.app module exists with all SQL scripts/tables/Setup.xml ✅
- MCAS inherits cas.app as Maven dependency ✅
- dependency-graph.json correctly lists cas.app ✅
- resource-plugin generates .resources.txt with all files ✅

**What Doesn't Work:**
- ResourceFileSystemProvider can't access nested JARs ❌
- SetupService looks for `setup/cas.app.setup.xml` but finds `setup/Setup.xml` ❌
- SQL scripts not executed during setup ❌

**The Specific Issue:**
```
CAS 13 cloud deployment:
  jar-dependencies/
    cas.app-13.6.2.jar  ← Separate file, accessible
    other-dep.jar

MCAS fat-jar deployment:
  aero.minova.service.mcas.jar
    BOOT-INF/lib/
      cas.app-13.6.2.jar  ← Nested JAR, NOT accessible to ClassLoader
```

**Immediate Solution (Workaround):**
- Copy cas.app's setup/sql/tables into MCAS source
- Violates DRY but works immediately
- Maintenance cost: sync when cas.app updates

**Proper Solution (Requires Investigation):**
- Study CAS 12 fat-jar build mechanism
- Understand how CAS 12 made dependency Setup.xml files accessible
- Modify resource-plugin to extract/organize dependency resources correctly
- Make ResourceFileSystemProvider work with fat-jar structure

### Phase 3: Unified Module System 🎯 FUTURE

**Goal:** Support both SQL and JPA modules in same MCAS instance

**Module Definition Options:**

**Option A: SQL-based (11/12 style)**
```xml
<setup name="com.customer.module">
    <sql-code>
        <script name="xpcasCustomerProcedure" type="procedure"/>
    </sql-code>
    <schema>
        <tableschema name="xtcusCustomerData" type="table"/>
    </schema>
</setup>
```

**Option B: JPA-based (13 style)**
```java
@Entity
@Table(name = "xtcusCustomerData")
public class CustomerData extends DataEntity {
    @Column private String customerName;
    // ... JPA annotations
}

// Liquibase migration
<changeSet id="1" author="developer">
    <createTable tableName="xtcusCustomerData">
        <column name="CustomerName" type="varchar(100)"/>
    </createTable>
</changeSet>
```

**Both work in same MCAS:**
- Auto-detect module type (Setup.xml vs @Entity)
- Run appropriate setup mechanism
- Both accessible via same REST API
- Gradual migration path for customers

---

## Technical Details

### CAS Database Access Framework

**Classic Framework (11/12/13 all use this):**
- Table objects (`aero.minova.cas.api.domain.Table`)
- Row/Column/Value objects
- SQL execution via stored procedures
- Used for:
  - REST endpoint `/data/view` → SQL views
  - REST endpoint `/data/procedure` → stored procedures
  - REST endpoint `/data/xprocedure` → extended procedures with error handling

**JPA Framework (13 attempted, partial):**
- Entity annotations
- Spring Data repositories
- Limited adoption due to conflicts with existing tables

**Reality:**
- Most DB operations still use classic framework
- JPA exists for some CAS internal tables
- Can't convert existing tables to JPA (on-premise conflicts)
- Classic framework will remain primary

### Resource System

**ResourceFileSystemProvider:**
- Custom ClassLoader-based file system
- Reads from `.resources.txt` files
- Lists available resources for each module
- Used by setup, i18n, forms, etc.

**Resource Discovery Process:**
1. Read `/aero.minova.app.resources/deployed.resources.txt`
2. Parse list of `.resources.txt` files for each module
3. Load each module's `.resources.txt`
4. Build complete resource list
5. Provide file access via custom Path implementation

**Problem with Fat-Jar:**
- Works for top-level module (MCAS itself)
- Fails for dependency modules (cas.app)
- ClassLoader can't see inside nested JARs in `BOOT-INF/lib/`

### Setup Mechanism

**SetupService:**
- Reads `dependency-graph.json` for dependency order
- For each dependency, finds its Setup.xml
- Pattern: `setup/{moduleName}.setup.xml` OR `setup/{moduleName}-*/Setup.xml`
- Installs tables (via InstallToolIntegration)
- Executes SQL scripts
- Sets up privileges

**Finding Setup.xml:**
```java
// SetupService.findSetupXml()
String niceSetupFile = dependency + ".setup.xml";  // "aero.minova.cas.app.setup.xml"
Path dependencySetupFile = dependencySetupsDir.resolve(niceSetupFile);

if (!exists) {
    // Fallback: walk directory looking for pattern
    // Expects: setup/cas.app-*/Setup.xml
    // Reality: setup/Setup.xml (cas.app is root setup)
}
```

**Why It Fails for cas.app:**
- cas.app is THE core module, not a customer extension
- Its Setup.xml is at `setup/Setup.xml` not `setup/aero.minova.cas.app.setup.xml`
- Pattern matching doesn't find it
- Resource provider can't access it in nested JAR anyway

---

## Migration Paths

### For 11-styled Apps → MCAS
**Status:** ✅ Complete and working

**Steps:**
1. Assist installs MCAS module
2. Assist writes application files → tFile table
3. Assist writes XBS registry → tRegistry table
4. Application accesses MCAS via REST API
5. MCAS reads from database instead of file system

**No code changes needed in modules!**

### For 12-styled Apps → MCAS
**Status:** 🔄 In progress (current work)

**Blockers:**
- cas.app Setup.xml not found in fat-jar
- SQL scripts not executed during auto-setup

**Once Fixed:**
- 12-styled CAS apps should work as MCAS
- Fat-jar deployment instead of cloud
- Auto-setup instead of manual /setup call

### For 13-styled Apps → MCAS
**Status:** 📋 Future work

**Considerations:**
- Only one 13 app exists (minimal impact)
- JPA/Liquibase already works in CAS
- Need to ensure coexistence with SQL-based setup
- May require hybrid setup mechanism

---

## Auto-Setup Feature

**Added:** 2025-12-03
**Purpose:** Eliminate manual POST /setup step for containerized MCAS

**Mechanism:**
- `ng.api.autosetup=true` in application.properties
- `AutoSetupService` runs on `@PostConstruct`
- Checks if `xtcasUsers` table exists
- Executes setup automatically if needed
- Same logic as manual `/setup` endpoint

**Current Status:**
- ✅ Detects if setup needed
- ✅ Executes setup procedure
- ❌ cas.app Setup.xml not found (fat-jar issue)
- ❌ SQL scripts not executed

**Once Fixed:**
- Zero-touch MCAS deployment
- Container starts → DB initialized → Ready
- True "micro" service behavior

---

## Open Questions for Future Work

### CAS 12 Build Investigation
- How did CAS 12 fat-jars organize dependency resources?
- Did CAS 12 have same nested JAR problem?
- What plugins/configuration made it work?
- Can we reuse CAS 12 approach?

### Resource Plugin Enhancement
- Should it extract dependency Setup.xml files?
- Should it rename them to match discovery pattern?
- How to handle multiple versions of same dependency?
- Performance impact of extracting all resources?

### Hybrid Setup System
- How to detect SQL vs JPA modules?
- Can both run in same transaction?
- Priority/ordering when both exist?
- Migration strategy from SQL → JPA?

### Backward Compatibility
- customer-build-project template: does it work?
- Existing 12-styled apps: will they break?
- On-premise Assist: still compatible?
- Module format: any breaking changes needed?

---

## Key Takeaways

1. **Don't assume JPA everywhere** - SQL procedures are primary, JPA is secondary
2. **11/12/13 are not versions** - they're architectural styles that must coexist
3. **Customer solutions are custom** - can't force migration, must support legacy
4. **MCAS is unification layer** - bridges all architectures into one back-end
5. **Fat-jar is new for CAS** - 13 was cloud-native, this is a retrofit
6. **Setup.xml is still critical** - JPA didn't replace it, they coexist

## Current Priority

**Fix cas.app Setup.xml discovery in MCAS fat-jar:**

Option 1 (Quick): Copy cas.app resources into MCAS source ← **Do this now**
Option 2 (Proper): Fix resource-plugin to extract dependencies ← **Investigate CAS 12 first**
Option 3 (Punt): Document manual setup step, disable auto-setup ← **Last resort**

**Recommendation:** Option 1 now, research Option 2 for proper fix later.

## Token Management Note
This conversation is at ~87% token usage. Remaining work should focus on implementing chosen solution without extensive research.
