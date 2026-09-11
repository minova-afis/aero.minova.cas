# Migrating a CAS 12 customer deployment to CAS 13

Step-by-step checklist for moving an existing CAS 12 customer repo/Dockerfile (e.g.
`com.minova.oiltanking.swb`, `aero.minova.invoice`) onto a CAS 13 base image, using the
backward-compatibility mechanism described in `doc/md/CAS12Compatibility.md`. This is the "keep the
customer repo unchanged, adapt only the Dockerfile/config" path — not a rewrite into the new
`cas.extension.parent`/`cas.extension.deployment` Maven structure.

## 1. Base image

```diff
-FROM minova/aero.minova.cas.app:12.71.3
+FROM minova/aero.minova.cas.app:13.x
```

Confirm the actual CAS 13 image tag/registry with whoever owns the release pipeline — at the time
of writing, no automated CAS 13 image build/push was found under `.github/workflows` in this repo
(the `docker-build-push` step in `.github/workflows/release.yml` publishes
`minova/aero.minova.cas.app`, but check the version/tag actually being produced before pointing a
customer image at it).

## 2. Dockerfile COPY destinations

The customer repo's own Maven build (`app.parent` parent POM, `docker-layer` /
`docker-extension-layer` generation) does **not** change. Only the two destination paths in the
customer's own `Dockerfile` change:

```diff
-COPY target/docker-layer /opt/aero.minova.cas/system-files/
-COPY target/docker-extension-layer /opt/aero.minova.cas/lib/
+COPY target/docker-layer /opt/aero.minova.cas/system-files-legacy/
+COPY target/docker-extension-layer /opt/aero.minova.cas/lib-legacy/
```

- `system-files-legacy/` is an arbitrary path — it just has to match whatever you set
  `legacy.system.files.path` to in step 3. `system-files-legacy` is only a suggested convention.
- `lib-legacy/` is **not** arbitrary — `/opt/aero.minova.cas/lib-legacy/` is the value baked into
  the base image's `ENV LOADER_PATH` (root `Dockerfile`). Use a different path only if you also
  override `LOADER_PATH` accordingly (see step 3).

## 3. Config / environment variables

Add (env vars or `application.properties`, however this deployment normally configures CAS):

```properties
# Already required for CAS 13 generally, independent of CAS-12 compatibility:
fat.jar.mode=true
aero_minova_core_application_root_path=/

# New: activates the CAS-12 on-disk directory (tables/sql/forms/i18n/setup/etc. from the old
# docker-layer output) -- checked FIRST, ahead of the fat jar, and used as a fallback only for
# whatever the legacy directory doesn't have.
legacy.system.files.path=/opt/aero.minova.cas/system-files-legacy
```

No property is needed to trigger the database setup step for the legacy directory either — it runs
automatically whenever `legacy.system.files.path` is set (see "Database setup" under step 4 below).
Just make sure the customer's legacy `setup/` (with `Setup.xml` and any `<dependency>.setup.xml`
files it needs) is included in whatever gets copied to `system-files-legacy/`.

No property is needed for extension jars in the common case — the base image already sets
`ENV LOADER_PATH=/opt/aero.minova.cas/lib-legacy/`, which customer Dockerfiles inherit
automatically, so step 2's `COPY` alone is enough to get old extension jars back on the classpath
at boot. **This must be an environment variable, not an `application.properties` entry** — Spring
Boot's `PropertiesLauncher` resolves `loader.path` before the classpath (and thus before any
nested-jar `application.properties`) exists, so setting it inside a properties file that ends up
packaged as a *dependency* jar (as opposed to the outermost jar) silently has no effect. If you
need a different legacy-lib path than the image default, override it explicitly:
```
--env LOADER_PATH=/opt/aero.minova.cas/lib-legacy/
```

Anything else the deployment already sets (DB connection, `login_dataSource`, SMTP env vars, etc.)
carries over unchanged.

### Cloud Deployment

Some cloud deployments (Kubernetes pod specs, kustomize patches, etc.) hardcode the
container's `command`/`args` instead of relying on the base image's default `ENTRYPOINT`/`CMD`,
usually to inject JVM system properties (truststore, JNDI/LDAP flags, ...). If a customer repo's
deployment does this, its `-cp`/main-class invocation is a leftover from CAS 12's exploded-layer
deployment style and needs to change too, or boot fails with:

```
Error: Could not find or load main class aero.minova.cas.CoreApplicationSystemApplication
Caused by: java.lang.ClassNotFoundException: aero.minova.cas.CoreApplicationSystemApplication
```

```diff
--cp "/opt/aero.minova.cas/lib/*" aero.minova.cas.CoreApplicationSystemApplication
+-jar /opt/aero.minova.cas/lib/aero.minova.cas.jar
```

Why: `/opt/aero.minova.cas/lib/aero.minova.cas.jar` is a Spring Boot repackaged executable
("fat") jar — its own classes live nested under `BOOT-INF/classes/` inside the jar, not at the
jar's root. `java -jar` works because Spring Boot's own loader (`JarLauncher`) knows how to unpack
`BOOT-INF/classes`/`BOOT-INF/lib` at runtime; that's also what the CAS 13 base image's own default
`CMD` does. Putting the outer jar on a plain `-cp` and naming
`aero.minova.cas.CoreApplicationSystemApplication` directly makes the JVM classloader scan only the
jar's root for that class — where it doesn't exist — hence the `ClassNotFoundException` above. This
only bit deployments that override `command`/`args`; anything using the base image's default launch
command was unaffected. Confirmed against `aero.minova.travelexpenses`'s cloud (dev) deployment,
which hardcoded the old `-cp`/main-class form in its kustomize pod patch — switching to `-jar` fixed
the boot failure.

## 4. Behavior changes to be aware of

- **MD5/zip caches are no longer pre-built at startup.** CAS 12 pre-computed `.md5` files and
  `.zip` bundles into `Internal/MD5`/`Internal/Zips` on disk at boot (`hashAll`/`zipAll`). In
  `fat.jar.mode`, those startup jobs are no-ops — `files/hash` and `files/zip` are computed
  on-the-fly per request instead (this now also covers files served from the legacy fallback
  directory). If anything outside CAS was reading those pre-built cache files directly from disk,
  it will no longer find them there.
- **Legacy files win over the fat jar when both exist for the same relative path.** If
  `legacy.system.files.path` is configured, that directory is treated as containing the complete,
  authoritative data for this customer — so a file under `system-files-legacy/` is served instead
  of a same-named CAS-13-native resource, not the other way around.
- **`upload/logs` is a no-op in `fat.jar.mode`.** User log uploads that CAS 12 stored under
  `Internal/UserLogs` are silently ignored in CAS 13's fat-jar mode (pre-existing CAS 13 behavior,
  unrelated to the legacy fallback — nothing to configure here, just be aware it doesn't write
  anywhere).
- **Database setup (`setup` procedure) runs twice when `legacy.system.files.path` is set.** Once
  against CAS 13's own native `Setup.xml`/schema/procedures (mandatory, aborts on error, unchanged
  behavior), and once more against the legacy `system-files-legacy/setup/Setup.xml` if present
  (optional — a missing legacy `Setup.xml` or `dependency-graph.json` is just logged, not an error).
  This is intentionally additive, not "legacy wins" like file serving: setup needs both schemas
  installed, not one instead of the other. See `doc/md/CAS12Compatibility.md`'s "Database setup"
  section for the full mechanics, including two pre-existing bugs in the Install-Tool/Setup code
  (`BaseSetup.readTableXml` and `SetupService.findSetupXml`'s fuzzy fallback) that had to be fixed
  for the legacy pass to work at all — the second one was actually caught via a real customer
  `setup` run crashing with `IllegalArgumentException`, not just found by inspection.

## 5. Verified against a real deployment

- File fallback (step 3, `legacy.system.files.path`) and the extension classpath addition (step 3,
  `ENV LOADER_PATH`) were confirmed working end-to-end against a real `com.minova.sky.swb`
  container running on a rebuilt CAS 13 base image: legacy `system-files` content was served
  correctly, and extension jars from `lib-legacy/` registered their Spring components correctly. An
  earlier version of the extension-loading fix (baking `loader.path` into `service`'s packaged
  `application.properties`) was tried first and confirmed broken in this same test — jars landed on
  disk but never registered — which is what led to the current `ENV LOADER_PATH` approach (see
  `doc/md/CAS12Compatibility.md` for the full diagnosis).
- Database setup (step 4): running `setup` against a real customer system originally surfaced the
  `SetupService.findSetupXml` crash described above. After the fix, this specific crash is covered
  by a regression test (`SetupServiceTest.fuzzyFallbackWorksAgainstRealDiskDirectory`), **and the
  fix was re-verified against that same real customer system** — both the CAS 13 native setup pass
  and the CAS 12 legacy setup pass were confirmed to execute successfully.

## 6. Known gap — not yet verified

- **Extension jar binary/API compatibility — accepted risk, explicitly not addressed.** Old
  extension jars were compiled against CAS 12's `cas.service`/`cas.setup` API surface and the
  CAS-12-era `registerExtension(...)` calling convention. Being on the classpath and registering
  (confirmed above) does not by itself prove every extension's behavior is still correct against
  CAS 13's current APIs. Because registration happens eagerly (`@PostConstruct`), a signature break
  hit on that path fails loudly at CAS boot — but a break inside the registered procedure/view
  logic itself stays latent until that specific procedure is actually invoked, possibly long after
  boot. Decision: not verified further (e.g. no diff of `Table`/`Row`/`Column`/`Value` and other
  extension-facing domain APIs between v12/v13) — will be diagnosed reactively if/when it comes up
  per extension, rather than pre-verified.
- Building the full reactor (`mvn package`/`install` from repo root or `-pl app -am`) currently
  fails in this environment at `cas.resource.maven.plugin:generate-resource-file` on `cas.app`
  (`ResourceListGenerator was not able to copy messages_de.properties ... `) — reproducible even
  from a clean `app/target`, and unrelated to any change in this document. Worth investigating
  separately; it blocked full-reactor verification of the setup changes here (verified instead via
  direct `compiler:compile`/`compiler:testCompile`/`surefire:test` goal invocations, which bypass
  the failing phase).

## 7. Rollback

Nothing in this migration is destructive to the customer repo itself — reverting is just reverting
the Dockerfile/env-var changes in steps 1–3 and pointing back at the CAS 12 base image.
