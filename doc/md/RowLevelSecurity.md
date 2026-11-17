# RowLevelSecurity im CAS

Mit der RowLevelSecurity (RLS) sieht ein Benutzer in einer View nur die Zeilen, die zu seinen *SecurityTokens* passen.
Diese Seite beschreibt die Mechanik so, wie sie im Code umgesetzt ist (`SecurityService`, `ProcedureService`, `MssqlViewService`),
und ersetzt das frühere `rowlevelexample.adoc`.

> **Konkrete Umsetzung:** Stundenerfassung, siehe
> [`aero.minova.workingtime/doc/md/rowlevelsecurity.md`](https://github.com/minova-afis/aero.minova.workingtime/blob/main/doc/md/rowlevelsecurity.md).
> Dort sind Gruppen, Views, Prozeduren und die Benutzeranlage vollständig ausgearbeitet.

## 1. Begriffe

| Begriff | Bedeutung |
|---|---|
| **UserSecurityToken** | Eindeutiger Token eines Benutzers (`xtcasUser.UserSecurityToken`, z.B. `#JAL`). Ohne Eintrag in `xtcasUser` ist der Token der Benutzername. |
| **Memberships** | Weitere Tokens/Gruppen des Benutzers (`xtcasUser.Memberships`, getrennt mit `#`). Bei LDAP kommen die AD-Gruppennamen hinzu. |
| **Gruppe** | Zeile in `xtcasUserGroup`: `KeyText` = Gruppen-/AD-Name, `SecurityToken` = Liste von Tokens (`#a#b#c`, max. 250 Zeichen). |
| **Privileg** | Name einer View, Prozedur oder Tabelle (`xtcasUserPrivilege.KeyText`). |
| **Zuordnung** | `xtcasLuUserPrivilegeUserGroup` verbindet Privileg und Gruppe, Spalte `RowLevelSecurity` (0/1). |

## 2. Funktionsweise

1. Beim Login sammelt CAS alle Tokens des Benutzers (UserSecurityToken, Memberships, AD-Gruppen).
   Für jede Gruppe, deren `KeyText` einem dieser Tokens entspricht, werden **alle Tokens aus `xtcasUserGroup.SecurityToken` ebenfalls an den Benutzer vergeben**.
2. Bei einer Anfrage werden aus `xvcasUserSecurity` die Zuordnungen des Privilegs für diese Tokens gelesen.
3. Ist bei **mindestens einer** passenden Zuordnung `RowLevelSecurity = 0`, sieht der Benutzer alles.
4. Sind alle passenden Zuordnungen `RowLevelSecurity = 1`, ergänzt CAS die View-Abfrage um

   ```sql
   where ( SecurityToken IS NULL ) or ( SecurityToken IN ('token1', 'token2', ...) )
   ```

   Die View **muss** dafür eine Spalte `SecurityToken` enthalten. Der Vergleich erfolgt in Kleinbuchstaben.
5. Gibt es keine passende Zuordnung, hat der Benutzer keinen Zugriff.

### Wichtige Konsequenzen

* **Zeilen mit `SecurityToken = NULL` sind für alle sichtbar.** Speichernde Prozeduren müssen den Token immer setzen, Altdaten müssen bewusst behandelt werden.
* **Gruppen vergeben ihre Tokens an alle Mitglieder.** Eine Gruppe `#a#b#c` lässt jedes Mitglied die Zeilen von a, b und c sehen
  (so bekommt ein Gruppenleiter die Zeilen seiner Mitarbeiter). Eine Gruppe mit den Tokens *aller* Mitarbeiter ist deshalb **kein** Weg, dass jeder nur seine eigenen Zeilen sieht.
  Dafür gibt es den Platzhalter `$user` (siehe Abschnitt 6): Er steht in der Gruppe für den Login-Namen des jeweiligen Mitglieds.
* **Prozeduren:** RLS filtert nur das *Ergebnis* (Output-Parameter bzw. ResultSet), und nur, wenn dieses eine Spalte `SecurityToken` enthält
  (sonst Fehler `msg.MissingSecurityTokenColumn`). Die Ausführung selbst (Insert/Update/Delete) wird **nicht** verhindert.
  Zugriffsprüfungen für schreibende Prozeduren müssen in der Prozedur erfolgen. Der Benutzername steht dort über `dbo.xfCasUser()` zur Verfügung, die Authorities (AD-Gruppen, Memberships) über den Session-Context, siehe [Abschnitt 7](#7-benutzer-und-gruppen-in-sql-prozeduren).

## 3. Anmeldeart und Datenquelle der Tokens

| `login_dataSource` | Quelle der Tokens |
|---|---|
| `ldap` | `xtcasUser` (UserSecurityToken, Memberships) + AD-Gruppen. Siehe [`adGroupsToUserGroups.md`](../../service/doc/adoc/adGroupsToUserGroups.md). |
| `database` | `xtcasAuthorities` (`Username`, `Authority`). `xtcasUser` wird **nicht** gelesen. Ohne Eintrag ist der Token der Benutzername. |
| `oidc` | Anmeldung über einen externen Identity Provider (OpenID Connect). Die Rollen aus dem Token des Providers werden zu Authorities. `xtcasUser` und `xtcasAuthorities` werden nicht gelesen. Fehlende Gruppen legt CAS in `xtcasUserGroup` ohne Rechte an, siehe [`Authentication.md`](Authentication.md). |

## 4. Beispiel

Die Tabelle `tJournal` und die View `vJournal` sollen zeilenweise geschützt werden.

### 4.1 Tabelle und View vorbereiten

```sql
alter table tJournal add SecurityToken varchar(50) null

alter view vJournal as
	select j.KeyLong, j.Description, j.SecurityToken   -- SecurityToken ist Pflicht
	from tJournal j
	where j.LastAction > 0
```

### 4.2 Gruppen

Die Gruppen entsprechen AD-Gruppen (`KeyText` = exakter Name der AD-Gruppe, siehe [`adGroupsToUserGroups.md`](../../service/doc/adoc/adGroupsToUserGroups.md)).
Der SecurityToken der Zeilen ist der **Login-Name** des Benutzers, der die Zeile angelegt hat.

```sql
-- Admin: sieht alles (RowLevelSecurity = 0)
insert into xtcasUserGroup (KeyText, SecurityToken) values ('G_SIS_SuperUser', '#G_SIS_SuperUser')

-- Standard: jedes Mitglied sieht nur die Zeilen mit seinem eigenen Login-Namen ($user, siehe Abschnitt 6)
insert into xtcasUserGroup (KeyText, SecurityToken) values ('G_SIS_User', '#$user#G_SIS_User')

-- Gruppenleiter: sieht zusätzlich die Zeilen seiner Mitarbeiter (deren Login-Namen als Tokens in SEINER Gruppe)
insert into xtcasUserGroup (KeyText, SecurityToken) values ('G_Leiter1', '#G_Leiter1#mitarbeiter1#mitarbeiter2')
```

Zu `G_Leiter1`: Jedes Mitglied der AD-Gruppe erhält alle Tokens aus der Liste. Deshalb steht dort der **Gruppenname** und nicht der Login-Name eines Leiters:
Mit einem Login-Namen in der Liste würde jedes Mitglied der Gruppe (z.B. ein zweiter Leiter) dessen Zeilen sehen.
Die eigenen Zeilen des Leiters kommen über `G_SIS_User` und `$user`. Der Token `G_Leiter1` selbst ist für den Filter nicht nötig, macht die Zuordnung aber lesbar.

### 4.3 Privilegien

```sql
declare @PrivilegeKey int = (select KeyLong from xtcasUserPrivilege where KeyText = 'vJournal')

insert into xtcasLuUserPrivilegeUserGroup (UserPrivilegeKey, UserGroupKey, RowLevelSecurity)
select @PrivilegeKey, KeyLong, 0 from xtcasUserGroup where KeyText = 'G_SIS_SuperUser'

insert into xtcasLuUserPrivilegeUserGroup (UserPrivilegeKey, UserGroupKey, RowLevelSecurity)
select @PrivilegeKey, KeyLong, 1 from xtcasUserGroup where KeyText in ('G_SIS_User', 'G_Leiter1')
```

> **Wichtig:** `RowLevelSecurity = 1` aktiviert die Filterung. `G_SIS_SuperUser` hat `0` und sieht deshalb alles.
> Ein Benutzer, der in `G_SIS_SuperUser` *und* einer Gruppe mit `1` ist, sieht ebenfalls alles.

### 4.4 Benutzer

Bei `login_dataSource=ldap` genügt die Mitgliedschaft in den AD-Gruppen, es ist **kein** Eintrag in `xtcasUser` nötig.
Ein Benutzer in `G_SIS_User` und `G_Leiter1` sieht seine eigenen Zeilen und die von `mitarbeiter1` und `mitarbeiter2`.

Beim lokalen Test mit `login_dataSource=database` kommen die Gruppen aus `xtcasAuthorities`:

```sql
insert into xtcasAuthorities (Username, Authority) values ('mitarbeiter1', 'G_SIS_User')
insert into xtcasAuthorities (Username, Authority) values ('chef', 'G_SIS_SuperUser')
```

Ist der SecurityToken der Zeilen **nicht** der Login-Name, kann der Token eines Benutzers in `xtcasUser.UserSecurityToken` stehen.
Dann braucht jeder Benutzer eine eigene Gruppe (`KeyText` = sein Token, `SecurityToken` = `#<Token>`) mit `RowLevelSecurity = 1` auf dem Privileg.

### 4.5 Schreibende Prozeduren

Der Token muss in der Prozedur ermittelt und beim Insert gespeichert werden. Am einfachsten ist der Login-Name als Token, er steht über `dbo.xfCasUser()` bereit:

```sql
insert into tJournal (..., SecurityToken)
values (..., dbo.xfCasUser())
```

Beim Update darf der Token nicht verändert werden. Wird ein Eintrag ohne Token gespeichert, ist er für alle sichtbar.

## 5. Fehlersuche

| Symptom | Ursache |
|---|---|
| Benutzer sieht alles | Eine passende Zuordnung hat `RowLevelSecurity = 0`, oder die Zeilen haben `SecurityToken = NULL`. |
| Benutzer sieht nichts | Er ist nicht in der Gruppe mit dem Privileg (AD-Gruppe falsch geschrieben, `KeyText` muss exakt passen), oder seine Zeilen haben einen anderen SecurityToken als sein Login-Name. Bei `database`-Login wird `xtcasUser` nicht gelesen. |
| Benutzer sieht Zeilen anderer | Eine seiner Gruppen enthält deren Tokens (Gruppen vergeben alle Tokens an alle Mitglieder), statt `$user` zu verwenden. |
| `msg.MissingSecurityTokenColumn` | Prozedur/View mit `RowLevelSecurity = 1` liefert keine Spalte `SecurityToken`. |
| Änderung an fremden Zeilen möglich | RLS wirkt nicht auf die Ausführung von Prozeduren, Prüfung in der Prozedur ergänzen. |

## 6. Platzhalter `$user`: eigene Zeilen ohne eigene Gruppe pro Benutzer

Ist der SecurityToken der Zeilen der **Login-Name** des Benutzers, braucht man keine Gruppe pro Benutzer und keinen Eintrag in `xtcasUser`.
Der Platzhalter `$user` in `xtcasUserGroup.SecurityToken` steht für den Login-Namen des angemeldeten Benutzers.
Mit der Gruppe `G_SIS_User` und dem SecurityToken `#$user#G_SIS_User` aus Abschnitt 4.2 gilt:

Ein Mitglied mit dem Login-Namen `janiak` bekommt dadurch den Filter `SecurityToken IS NULL OR SecurityToken IN ('janiak', 'g_sis_user')`.

Regeln:

* Der Platzhalter wirkt nur, wenn die Gruppe **mindestens einen weiteren Token** enthält, den der Benutzer besitzt (hier `G_SIS_User`, also die AD-Gruppe).
  Ohne diese Mitgliedschaft wird er ignoriert, und jeder andere Benutzer, der die Gruppe nicht hat, bekommt dadurch keine Rechte.
* Der Platzhalter wird **nicht** als Authority an den Benutzer vergeben. Er erscheint nicht in den Tokens des Benutzers und nicht in `casAuthorities`.
* Er kann in der Liste an beliebiger Stelle stehen und mit weiteren Tokens kombiniert werden (z.B. `#$user#G_SIS_User#leiter`).
* Die Prozeduren müssen den Login-Namen als Token speichern, siehe Abschnitt 4.5 (`dbo.xfCasUser()`).
* Tokens werden vor dem Einbau in die SQL-Abfrage maskiert (Hochkommas).

## 7. Benutzer und Gruppen in SQL-Prozeduren

Das CAS setzt vor jedem Prozeduraufruf (SQL Server: `sys.sp_set_session_context`, PostgreSQL: `SET my.app_...`) zwei Werte:

| Wert | Inhalt | SQL Server | PostgreSQL |
|---|---|---|---|
| `casUser` | Login-Name | `dbo.xfCasUser()` | `my.app_user` |
| `casAuthorities` | Authorities (AD-Gruppen, Memberships, Rollen) als `#a#b#c#`, höchstens 4000 Zeichen | `session_context(N'casAuthorities')` | `my.app_authorities` |

Damit kann eine Prozedur prüfen, ob der Benutzer Mitglied einer AD-Gruppe ist, ohne dass `xtcasUser` gepflegt wird:

```sql
declare @Authorities nvarchar(4000) = convert(nvarchar(4000), session_context(N'casAuthorities'))
if @Authorities like '%#G[_]SIS[_]SuperUser#%'
	-- darf alles
```

`_` muss in `like` mit `[_]` maskiert werden. Die Werte werden nur bei Prozeduraufrufen gesetzt, nicht bei View-Abfragen.

> Hinweis: Dieses Dokument wurde gegen den Code geprüft, die Stundenerfassung dient als praktischer Test.
