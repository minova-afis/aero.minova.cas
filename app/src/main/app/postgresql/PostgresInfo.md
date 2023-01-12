# Postgres Setup

Alle Tabellen/Views/Funktionen müssen per Hand eingespielt werden. 
In der Datei `app/src/main/app/postgres/postgresSetup.sql` sind alle in einem File zusammengefügt.
Es reicht, dieses einzuspielen.

## Rechte

Auch die Rechte müssen händisch aufgesetzt werden.

Option 1 (über SQL Skripte) 
1. Alle benötigten Privileges (Prozedur-/Viewnamen, ...) in Tabelle `xtCasUserPrivilege` eintragen
2. Benutzer in `xtcasUsers` erstellen
3. Gruppe in `xtcasUserGroup` erstellen
4. Benutzer und Gruppe über `xtcasAuthorities` verbinden
5. Für jeden Privileg einen Eintrag in `xtcasLuUserPrivilegeUserGroup` erstellen, das Privileg mit der Gruppe verbindet

Option 2 (JPA bzw. Controller im CAS, muss aus CAS-Extension aufgerufen werden)
1. `AuthorizationController` über `@Autowired` laden
2. Über `authorizationController.createUserPrivilege("privilegeName")` alle Privilegien erstellen
3. Über `authorizationController.createAdminUser("username", "encryptedPassword")` einen Nutzer erstellen, der alle erstellten Berechtigungen besitzt 

Dieser Code kann bei jedem Start aufgerufen werden.

**WICHTIG**: in den `application.properties` muss folgender Eintrag stehen, damit in Postgres Namen von Tabellen/Prozeduren/Spalten/... nicht mit `_` generiert werden:

````
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
````
