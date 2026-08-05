# Authentication & Access Management

This is the developer-facing reference for **how a CAS instance authenticates users** — every mode
that exists today, plus the one that's planned — and a short orientation on how **access management**
(who is allowed to do what) works on top of that. For the deep-dive mechanics of the privilege/
row-level-security model, see [`service/doc/adoc/security.adoc`](../../service/doc/adoc/security.adoc)
and [`service/doc/adoc/nutzerverwaltung.adoc`](../../service/doc/adoc/nutzerverwaltung.adoc) (German) —
this doc doesn't replace those, it's the property-focused "what do I actually set" companion, including
the planned OIDC/Keycloak mode those don't cover yet.

## Selecting a mode

One property controls everything: `login_dataSource`. It can be set either directly in
`application.properties`, or as an environment variable in a container/cloud deployment — Spring Boot's
relaxed property binding treats an env var like `LOGIN_DATASOURCE` as equivalent to the property
`login_dataSource`, case-insensitively. Both forms are in active use across existing CAS deployments;
use whichever fits your deployment style.

| `login_dataSource` | Status | What it does |
|---|---|---|
| `admin` | Available | Single hardcoded in-memory user, for local testing only |
| `database` | Available | Users/passwords/authorities stored in CAS's own `xtcasUsers`/`xtcasAuthorities` tables |
| `ldap` | Available | Authenticates against Active Directory |
| `oidc` | **Planned, not implemented yet** | Bearer-token (OIDC) auth via the shared `foundation.rest.auth` library — Keycloak, Azure AD/Entra ID, or any standards-compliant provider |

---

## `admin` — in-memory test user

```properties
login_dataSource=admin
```

Creates one in-memory user: username `admin`, role `ADMIN`. **The password is a hardcoded literal in
`SecurityConfig.java` — there is no property or environment variable to change it.** This mode exists
for local testing only and must never be used in a real deployment (the shipped
`application.properties` already carries a comment to that effect).

Note: newer versions of the auto-setup flow (`AutoSetupService`) create their own temporary,
short-lived security context for initial database bootstrapping, so you likely no longer need
`login_dataSource=admin` just to get through first-time setup — check whether your use case actually
needs it before reaching for this mode.

---

## `database` — CAS-managed users

```properties
login_dataSource=database
spring.datasource.url=jdbc:sqlserver://localhost;encrypt=false;databaseName=AFIS_HAM
spring.datasource.username=sa
spring.datasource.password=password
```

(Environment-variable form: `LOGIN_DATASOURCE=database`, `SPRING_DATASOURCE_URL=...`,
`SPRING_DATASOURCE_USERNAME=...`, `SPRING_DATASOURCE_PASSWORD=...` — the last one should come from a
secret store/Kubernetes Secret, not a plain manifest value.)

Users and authorities live in CAS's own tables (`xtcasUsers`, `xtcasAuthorities`), managed via the same
datasource CAS already uses for everything else — no extra connection config needed beyond the
standard `spring.datasource.*` properties. Passwords are encoded via CAS's own `PasswordEncrypter`.
See [`nutzerverwaltung.adoc`](../../service/doc/adoc/nutzerverwaltung.adoc) for how to actually create
users in this mode, and [`service/doc/md/UserRights.md`](../../service/doc/md/UserRights.md) /
[`RechteverwaltungCAS-WFCüberdieSQL-Datenbank.md`](../../service/doc/md/RechteverwaltungCAS-WFCüberdieSQL-Datenbank.md)
for the WFC-UI and raw-SQL workflows.

---

## `ldap` — Active Directory

```properties
login_dataSource=ldap
security_ldap_domain=minova.com
security_ldap_address=ldap://mindcsrv.minova.com:3268/
```

(Environment-variable form: `LOGIN_DATASOURCE=ldap`, `SECURITY_LDAP_DOMAIN=...`,
`SECURITY_LDAP_ADDRESS=...`.)

Authentication binds directly as the logging-in user (there is **no service-account/bind-DN
property** — CAS never stores a separate LDAP credential). No LDAP connection-timeout properties
exist either; Spring LDAP's defaults are used as-is.

**Multiple domains/servers** are supported by separating values with `;`:

```properties
security_ldap_domain=minova.com;minova.de
security_ldap_address=ldap://dc1.minova.com:3268/;ldap://dc2.minova.de:3268/
```

Matching rules: equal counts pair up 1:1 in order; a single address applies to all domains; a single
domain applies to all addresses; any other count mismatch fails startup with an explicit error. Users
with the same username across different domains are treated as the same CAS user.

After a successful bind, CAS merges the user's AD group memberships into its own privilege model — see
[Access Management](#access-management) below and
[`adGroupsToUserGroups.md`](../../service/doc/adoc/adGroupsToUserGroups.md) for wiring AD groups to
CAS's `xtcasUserGroup` entries.

---

## `oidc` — planned, not implemented yet

This mode does not exist in CAS today — this section documents the **intended** design, based on the
shared [`foundation.rest.auth`](https://github.com/minova-afis/foundation.rest.auth) library, so it's
ready to fill in once the integration lands. Do not set these properties expecting them to work yet.

The intended shape (`foundation.rest.auth`'s standard OIDC properties, plus `login_dataSource=oidc` as
a new fourth value alongside today's three):

```properties
login_dataSource=oidc
foundation.rest.auth.mode=oidc
foundation.rest.auth.oidc.issuer-uri=https://saas-sso.minova.com/realms/afis
foundation.rest.auth.oidc.client-id=minova-hub
foundation.rest.auth.oidc.roles-claim=realm_access.roles
foundation.rest.auth.oidc.roles-prefix=
```

The `issuer-uri`/`client-id`/`roles-claim` values above are MINOVA's shared Keycloak SSO realm — the
same one `ch.minova.assist.jettyserver`'s Hub already uses by default — safe to start from as-is;
swap in a per-service `client-id` once one is provisioned for CAS specifically. `roles-prefix` is set
to empty deliberately: `foundation.rest.auth` normally prefixes each JWT role with `ROLE_` so Spring's
own `hasRole(...)` works out of the box, but CAS doesn't use `hasRole()`/`hasAuthority()` anywhere — it
matches raw role strings directly against its own `xtcasUserGroup`/`xtcasUserPrivilege` tables (see
below), so the prefix would just be noise that has to be stripped or worked around otherwise.

Azure AD/Entra ID would need no CAS-side code change at all once this lands — only different property
values (a different `issuer-uri` shape and a `roles-claim` of `roles` instead of
`realm_access.roles`), since `foundation.rest.auth`'s OIDC mode is provider-agnostic by design.

Because CAS's existing `SecurityFilterChain` is unscoped (matches every request path) and doesn't use
declarative Spring Security expressions, the integration can't simply add
`foundation.rest.auth`'s own auto-configured filter chain alongside CAS's — it needs to exclude that
part and reuse just the underlying `JwtDecoder`/`JwtAuthenticationConverter` beans
(`ch.minova.foundation.rest.auth.config.OidcJwtConfig`) inside CAS's own chain. See that class's
Javadoc in the `foundation.rest.auth` repo for the intended pattern.

An OIDC-authenticated user's role(s) still have to be wired into CAS's privilege tables exactly like
any other role — an unrecognized role grants **no** access by default (see the next section) — a
planned auto-provisioning step will create the missing entry with zero privileges automatically
(full privileges only for the literal `admin` role), so an operator only has to grant what's needed
rather than build the entry from scratch.

---

## Access Management

However a user authenticates, **what they're allowed to do is decided by a completely separate,
data-driven layer** — not by which login mode they used. CAS does not use Spring Security's
declarative `hasRole()`/`hasAuthority()`/`@PreAuthorize` anywhere; access decisions are made by
matching the exact granted-authority strings on the current `Authentication` against rows in CAS's own
tables (`xtcasUserGroup.SecurityToken`, joined through `xtcasLuUserPrivilegeUserGroup` to
`xtcasUserPrivilege`, exposed via the `xvcasUserSecurity` view). This is checked on essentially every
data-access endpoint (procedures, views, files) — a granted authority that has no matching
`xtcasUserGroup` row grants **zero** access, regardless of how plausible-looking the role name is (a
role literally named `admin` is not special-cased in this layer at all — it's just data, wired up the
same way as any other role would be).

Row-level and column-level security build on the same mechanism: `xtcasLuUserPrivilegeUserGroup.RowLevelSecurity`
and `xtcasColumnSecurity` further restrict *which rows/columns* a matched privilege actually exposes.

This is intentionally just an orientation, not the full mechanics — see:
- [`security.adoc`](../../service/doc/adoc/security.adoc) — the full SecurityToken/row-level/column-level model
- [`rowlevelexample.adoc`](../../service/doc/adoc/rowlevelexample.adoc) — a worked SQL example
- [`adGroupsToUserGroups.md`](../../service/doc/adoc/adGroupsToUserGroups.md) — wiring AD groups into this model
- [`UserRights.md`](../../service/doc/md/UserRights.md) / [`RechteverwaltungCAS-WFCüberdieSQL-Datenbank.md`](../../service/doc/md/RechteverwaltungCAS-WFCüberdieSQL-Datenbank.md) — the WFC-UI and raw-SQL workflows for actually creating groups/privileges

### Outlook: a common way to check/control grants

This data-driven, endpoint-name-agnostic model exists because CAS's actual endpoints are few and
generic (execute this named procedure, read this named view) — access has to be decided from the
*contents* of a request, not from the URL, which doesn't map cleanly onto Spring's usual
per-endpoint-role annotations. That said, the *mechanism itself* (checking "may this principal do X to
target Y") is exactly what Spring Security's `PermissionEvaluator`/`hasPermission(...)` expression is
designed for — CAS's original implementation predates that being a known option here, which is why it
was built as an imperative, manually-invoked check duplicated across a handful of controllers rather
than a single declarative one.

**It's planned to extend `foundation.rest.auth` with a common, reusable way to check and control
grants** (roughly: a shared `PermissionEvaluator` contract, and guidance for database-native row-level
security for services not using JPA), so future services — and eventually CAS itself — don't each
reinvent this. Not implemented yet; noted here so it's not a surprise when it lands.
