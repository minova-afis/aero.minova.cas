# CAS 12 -> CAS 13 backward compatibility

Goal: let existing CAS 12 customer deployments (e.g. `com.minova.oiltanking.swb`,
`aero.minova.invoice`) run against a CAS 13 core without every customer repo having to be
migrated into the new fat-jar Maven build (`cas.extension.parent`/`cas.extension.deployment`).

## How the two versions differ

### CAS 12 — real files, real classpath

- No fat jar. The container has two on-disk trees, both populated by `COPY` in the customer's own
  Dockerfile on top of a versioned `minova/aero.minova.cas.app:12.x` base image:
  - `/opt/aero.minova.cas/lib/` — a flat classpath: CAS jars + every dependency jar + every
    customer extension jar. Launched either via `-cp lib/*` or via Spring Boot's
    `PropertiesLauncher` (`loader.path=lib/`). No runtime scanning — jars just need to be on the
    classpath at boot for Spring to pick up their `@Component`s.
  - `/opt/aero.minova.cas/system-files/` — real sql/tables/forms/i18n/setup files, pointed to by
    `aero_minova_core_application_root_path`.
- `FilesService` resolves everything with plain `java.nio.file.Paths`/`File` against that root.
- Customer repos never change to get a new CAS version — they just rebuild their own
  `docker-layer`/`docker-extension-layer` output (via the shared `app.parent` Maven POM) and layer
  it onto whichever base image tag they choose.

### CAS 13 — fat jar, classpath-as-virtual-filesystem

- Every module's resources (CAS's own `cas.app` resources *and* every extension's resources) are
  compiled into one Spring Boot fat jar (`cas.extension.deployment`, `layout=ZIP`).
- `ResourceFileSystemProvider` (`service/src/main/java/aero/minova/cas/resources/ResourceFileSystemProvider.java`)
  fakes a read-only `Path`/`FileSystem` on top of `ClassLoader.getResourceAsStream(...)`, driven by
  a generated manifest (`/aero.minova.app.resources/deployed.resources.txt`) assembled at build
  time from every dependency's own resource-list manifest.
- `FilesService.setUp()` (`FilesService.java:93-126`) already forks on
  `fat.jar.mode`: `true` -> virtual `ResourcePath` rooted via that provider (read-only, no MD5
  caching to disk, zips built on the fly per request); `false` -> exactly CAS 12's plain-disk
  behavior.
- Extensions are no longer runtime-loaded jars — they're compile-time Maven dependencies of the
  fat-jar build, registered via `registerExtension(...)` at Spring context startup. The "drop a
  jar in `lib/`" mechanism from CAS 12 doesn't exist anymore in this design.
- Known caveat (`doc/md/BaseArch.md`, unresolved as of authoring): dependency modules nested
  inside `BOOT-INF/lib/*.jar` can have visibility problems with their own `Setup.xml`/sql/tables
  through this same provider. This is a reason to prefer a disk-fallback over extending reliance
  on the classpath-resource mechanism further.

## Chosen approach: disk-fallback, not repackaging old repos

Rejected: migrating CAS 12 customer repos into the CAS 13 Maven module structure just for
compatibility. That amounts to a full re-release of every customer repo against CAS 13 APIs — a
migration project, not a compatibility shim — and it piles more load onto a resource-loading
mechanism that's already documented as fragile for nested dependencies.

Instead: give CAS 13's existing resource resolution a **two-tier lookup** when running in fat-jar
mode — check a configured on-disk legacy directory *first*, and only fall back to the fat jar's
classpath resources if nothing is found there. That legacy directory is exactly
`/opt/aero.minova.cas/system-files/`, the path CAS 12 customer Dockerfiles already populate — so
**CAS 12 customer repos need zero changes**. They keep building `docker-layer` the old way; the
operator just also mounts/copies that output into the CAS 13 image at a configured legacy path.

Legacy wins over the fat jar when both exist for the same path, not the other way around: if
`legacy.system.files.path` is configured at all, that directory is treated as containing the
complete, authoritative data set for that specific customer installation, so it should be able to
override a same-named CAS-13-native (often generic/default) resource — see the "Legacy wins over
the fat jar" bullet under "What changed" below for the full rationale.

## What changed

New config property (default empty = fallback disabled):

```properties
legacy.system.files.path=/opt/aero.minova.cas/system-files
```

### `service/src/main/java/aero/minova/cas/service/FilesService.java`

- `legacySystemFilesPath` field bound to `legacy.system.files.path`.
- `hasLegacyFallback()` — whether the property is set.
- `getLegacyRoot()` — normalized absolute path to the legacy directory.
- `resolveLegacyFile(String relativePath)` — resolves a single relative path against the legacy
  root, guards against path escapes (`resolved.startsWith(legacyRoot)`), returns
  `Optional<Path>` present only if the file actually exists.
- `listLegacyFiles(String relativePrefix)` — same escape-guard, returns every file under a legacy
  sub-directory (reuses the existing `populateFilesList` walk) for the zip-bundle fallback case.

### `service/src/main/java/aero/minova/cas/controller/FilesController.java`

- `getIncludedFile(String path)`: in fat-jar mode, `fileService.resolveLegacyFile(path)` is tried
  **first**; only if that doesn't find anything does it fall through to
  `getClass().getResourceAsStream(path)`. This alone covers `files/read` directly, and transitively
  covers `files/hash` (its fat-jar branch calls back into `getFile(...)` for live MD5 calculation) —
  no separate change needed for hashing.
- `getZip(String path)`: in fat-jar mode, legacy files from `fileService.listLegacyFiles(pathStr)`
  are written into the on-the-fly zip **first**; classpath resources matching the requested prefix
  from `deployed.resources.txt` are added afterwards, skipped if an entry of the same relative path
  was already written from the legacy directory.
- **Legacy wins over the fat jar when both exist for the same path**, not the other way around.
  Rationale: if `legacy.system.files.path` is configured at all, that directory is understood to
  contain the complete, authoritative data set for that specific customer installation — a
  same-named CAS-13-native resource is more likely to be a generic/default file that the customer's
  own legacy data is meant to override, not the reverse. (An earlier version of this had the
  precedence flipped the other way; changed after review.)

### Not touched (by design)

- `hashAll()` / `zipAll()` / `createZip()` stay no-ops in fat-jar mode, exactly as before — they
  already only exist to pre-build MD5/zip caches for the plain-disk mode; fat-jar mode always
  computes on demand, and the legacy fallback flows through that same on-demand path automatically.
- `checkLegalPath()` / the non-fat-jar (`isFatJarMode=false`) branches are untouched — that code
  path is already CAS 12's exact behavior and doesn't need a fallback.

## Extension jars: not a fallback — an always-on classpath addition

Unlike file resolution, extension loading can't be a "try CAS 13's way, then fall back to CAS 12's
way" per-request check: by the time a request arrives, the JVM's classloader/`@ComponentScan` has
already run once at boot. Either a legacy extension class is on the classpath before Spring starts,
or it never exists in that run. So this has to be an unconditional, always-checked step at startup,
not a conditional fallback gated behind some primary lookup failing.

CAS 13's fat jar is already built with `layout=ZIP` (`cas.extension.deployment/pom.xml`), so Spring
Boot's `PropertiesLauncher` and its `loader.path` mechanism are available "for free" — the same
launcher/property CAS 12 itself used for its own single-fat-jar launch variant (see
`service/doc/adoc/installation.adoc:108`, `-Dloader.path="lib/" -jar aero.minova.cas.service.jar`).
`loader.path` entries are resolved by the launcher *before* the Spring context (and thus before any
`@Value`/Environment-based config) exists, so it must be a plain `key=value` line in a properties
file on the classpath, an environment variable (`LOADER_PATH`), or a JVM system property
(`-Dloader.path=`) — not something our own Spring beans can read or control at request time.

### What changed

`service/src/main/resources/application.properties` had a leftover CAS-12-era line:

```properties
loader.path=../lib/
```

An initial attempt at this fix changed that line to an absolute path
(`/opt/aero.minova.cas/lib-legacy/`), on the theory that `PropertiesLauncher` reads `loader.path`
out of whatever `application.properties` ends up on the classpath. **That attempt was wrong and was
confirmed broken by an actual container test**: extension jars were copied to
`/opt/aero.minova.cas/lib-legacy/` correctly, but their Spring components never registered.

The reason: `PropertiesLauncher` resolves `loader.path` at bootstrap, *before* the classpath is
built from `BOOT-INF/lib/*.jar` dependencies — it only reads a properties file from the **outer**
jar's own root. `service` (`cas.service.jar`) is packaged as a *nested* dependency jar inside
`customer-build-project`'s repackaged fat jar (which has no `src/main/resources` of its own by
design). So a `loader.path` line inside `service`'s `application.properties` is invisible to the
launcher in that deployment shape — it only would have worked if `cas.service.jar` were run
standalone as the outer jar itself (e.g. the manual/non-Docker install path in
`installation.adoc`).

This is different from `legacy.system.files.path` (file fallback), which is read via a normal
Spring `@Value` field *after* the Spring context has already started — by which point the merged
classpath (including everything nested under `BOOT-INF/lib/*.jar`) is visible to ordinary property
resolution. `loader.path` has no such luxury; it must come from something the launcher checks
*before* any classpath/context exists at all: an environment variable, a JVM system property, or a
manifest attribute — never a properties file nested inside a dependency jar.

**Actual fix**: reverted `service/src/main/resources/application.properties`'s `loader.path` back
to `../lib/` (with a comment explaining it only applies if this jar is ever run standalone), and
instead set it as an environment variable on the root `Dockerfile`:

```dockerfile
ENV LOADER_PATH=/opt/aero.minova.cas/lib-legacy/
```

Since customer Dockerfiles do `FROM minova/aero.minova.cas.app:13.x`, they inherit this `ENV`
automatically — no extra customer-side config needed, matching the "always checked" requirement.
If the directory doesn't exist (no legacy extensions), `PropertiesLauncher` skips it silently, so
this is safe to leave permanently enabled by default. Operators can still override it via
`--env LOADER_PATH=...` or `-Dloader.path=` at container launch, which take precedence over the
image's baked-in `ENV` default.

**Verified against a real deployment**: this was caught via an actual `docker run` test against
`com.minova.sky.swb` — jars were landing on disk correctly under `/opt/aero.minova.cas/lib-legacy/`
(the `COPY` side always worked), but their Spring components never registered, which is what
exposed the `PropertiesLauncher` bootstrap-timing issue above. After rebuilding the CAS base image
with `ENV LOADER_PATH=/opt/aero.minova.cas/lib-legacy/` in the root `Dockerfile` and rebuilding/
restarting the customer container, the extensions registered correctly — confirming the `ENV`-based
fix works and the earlier packaged-`application.properties` approach did not.

## Database setup: run both, native mandatory / legacy optional

The `setup` procedure (`SqlProcedureController` dispatches `{"name":"setup",...}` to
`SetupService`, `app/src/main/java/aero/minova/cas/setup/SetupService.java`) reads a
`setup/Setup.xml` (plus per-dependency `setup/<dependency>.setup.xml` files listed in
`setup/dependency-graph.json`) and, via `InstallToolIntegration`/`BaseSetup` (the Install-Tool),
creates/updates the DB schema and runs SQL scripts referenced from those files.

Unlike file serving, this is **not** a "legacy overrides native" precedence — it's additive. A
customer's legacy `system-files-legacy/setup/Setup.xml` describes CAS-12-era schema/procedures that
are just as necessary as CAS 13's own; skipping either one would leave the database missing
something. So when `legacy.system.files.path` is configured, `SetupService.setup()` now runs the
whole setup process **twice**:

1. Against `service.getSystemFolder()` (CAS 13 native, fat jar or real disk) — unchanged behavior,
   a missing main `Setup.xml` here is still a hard failure (`NoSuchFileException`).
2. Against `service.getLegacyRoot()`, **only if `hasLegacyFallback()`** — new. A missing
   `dependency-graph.json` or main `Setup.xml` in the legacy tree is **not** fatal — it's logged and
   skipped, since not every customer with a `legacy.system.files.path` (set for file-serving
   purposes) necessarily has DB-relevant legacy setup content there.

### What changed

- `InstallToolIntegration.installSetup(Path setupXml)` now delegates to a new
  `installSetup(Path setupXml, Path filesRoot)` overload, which resolves `tables`/`sql` relative to
  the given `filesRoot` instead of always hardcoding `files.getSystemFolder()`. This is what makes
  it possible to point a single install call at either root.
- `SetupService.readSetups(...)` gained two parameters: `filesRoot` (threaded through to
  `installSetup`) and `required` (whether a missing main `Setup.xml` throws or is just logged). It
  also no longer assumes `dependency-graph.json` exists — if it's missing, dependency processing is
  skipped (logged) instead of throwing, since CAS-12 legacy trees don't produce this file (it's
  generated by CAS 13's own `depgraph-maven-plugin` step).
- `SetupService.setup()` calls `readSetups(...)` once for the native root (`required=true`, as
  before) and, if `service.hasLegacyFallback()`, a second time for `service.getLegacyRoot()`
  (`required=false`).
- **Two pre-existing bugs fixed as hard prerequisites for this to work at all** — both are the same
  root cause in two places: code that assumed `Path` arguments are always classpath `ResourcePath`s
  and called `FILE_SYSTEM_PROVIDER.walk(...)` unconditionally, which throws `IllegalArgumentException`
  for anything that isn't a `ResourcePath`. A real disk path (as the legacy pass necessarily uses)
  would crash immediately:
  - `BaseSetup.readTableXml` (`app/src/main/java/ch/minova/install/setup/BaseSetup.java`) had a
    literal `if (true)` that always took the classpath-only branch. Fixed to branch on
    `tableLibrary.get() instanceof ResourcePath`, using the (previously dead) `Files.walk(...)`
    branch for real disk paths.
  - `SetupService.findSetupXml`'s fuzzy fallback lookup (used when a dependency's `Setup.xml` isn't
    found under its exact expected name, `<dependency>.setup.xml`) had the identical unconditional
    `FILE_SYSTEM_PROVIDER.walk(dependencySetupsDir)` call. **This one was caught the hard way**: it
    surfaced as `java.lang.IllegalArgumentException: null` at
    `ResourceFileSystemProvider.walk(...)` → `SetupService.findSetupXml` → `readSetups` while
    running `setup` against a real customer system, because in that system a dependency's
    `Setup.xml` didn't exist under its exact expected name and fell through to the fuzzy walk
    against the (real disk) legacy `setup/` directory. Fixed the same way: branch on
    `dependencySetupsDir instanceof ResourcePath`, and for the real-disk case use `Files.walk(...)`
    with a relativized, `"setup/"`-prefixed path string to preserve the exact same (admittedly
    hacky, string-prefix-based) matching semantics as the classpath branch.
  - Both incidentally fix the identical latent crash for plain non-fat-jar-mode CAS 13 deployments,
    which hit the same bug independent of any legacy-compatibility configuration.

### Verification

Covered by `SetupServiceTest` (`app/src/test/java/aero/minova/cas/setup/SetupServiceTest.java`)
exercising `readSetups`/`findSetupXml` directly with mocked `InstallToolIntegration`/`CustomLogger`
and real temp files — no Spring context or database needed. Confirms: a missing
`dependency-graph.json` doesn't abort the run, a missing required main `Setup.xml` throws, a
missing optional one doesn't, `installSetup` is called with the exact `filesRoot` passed in (not
silently falling back to `getSystemFolder()`), and — the regression test for the real crash above —
the fuzzy fallback lookup finds a real-disk `Setup.xml` via a non-exact-name match without throwing
`IllegalArgumentException`. **Verified end-to-end**: after the fix, `setup` was re-run against the
same real customer system that originally surfaced the `findSetupXml` bug, and both the CAS 13
native setup pass and the CAS 12 legacy setup pass executed successfully.

## Known gap: extension jar binary compatibility (deferred)

This assumes old extension jars (compiled against CAS 12's `cas.service`/`cas.setup` APIs and the
`registerExtension(...)` calling convention from that version) still link and behave correctly
against CAS 13's current API surface once they're on the classpath. That's a binary-API-compatibility
question, not a classpath-loading one, and hasn't been verified here. Per decision: treated as safe
for now; if it turns out old extension jars don't load or misbehave once actually on the classpath,
that will be diagnosed and fixed separately from the loading mechanism above.

## Operator-facing summary

See `doc/md/CAS12ToCAS13Migration.md` for the full step-by-step checklist (Dockerfile diff, config
properties, behavior changes, and known gaps to verify per customer before relying on this in
production).
