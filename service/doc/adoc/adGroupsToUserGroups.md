# Vergeben von Berechtigungen für AD-Gruppen

Damit der User über seine AD-Gruppen Privilegien erhalten und nutzen kann,
müssen folgende Schritte befolgt werden:

## 1. application.properties

Die *login_dataSource* des CAS muss in den application.properties auf *'ldap'* gesetzt werden.

## 2. Eintrag in xtcasUserGroup

Die AD-Gruppe muss in der Datenbank in die xtcasUserGroup eingetragen werden.

Hierbei muss die Schreibweise des Gruppennamens ganz genau beibehalten werden.
Die SecurityToken dürfen jedoch abweichen bzw. auch komplett anders heißen.

> Beispiel: Die AD-Gruppe heißt 'BeiSPielGrupPpE'.

````
-- AD-Gruppenname = BeiSPielGrupPpE

insert into xtcasUserGroup (KeyText,SecurityToken) values ('BeiSPielGrupPpE','#adgruppe')
````

## 3. Privilegien der Gruppe zuweisen

Da nun die AD-Gruppe mit der xtcasUserGroup in der Datenbank verbunden ist,
können über die xtcasLuUserPrivilegesUserGroup die Privilegien an die Gruppe verknüpft werden.

> Beispiel: Die Gruppe 'BeiSPielGrupPpE' darf die View 'vBeispiel' und die Prozedur 'pBeispiel' aufrufen.

````sql
-- AD-Gruppenname = BeiSPielGrupPpE
declare @UserGroupKey int,
		@UserPrivilegeKey int
		
select @UserGroupKey = KeyLong from xtcasUserGroup where KeyText = 'BeiSPielGrupPpE'

select @UserPrivilegeKey = KeyLong from xtcasUserPrivilege where KeyText = 'BeispielProzedurOderView'

insert into xtcasLuUserPrivilegeUserGroup (UserGroupKey, UserPrivilegeKey, LastDate, LastUser, LastAction, RowLevelSecurity) values ( @UserGroupKey, @UserPrivilegeKey, getDate(), 'support', 1, 1)

````


## 4. Überprüfung der AD-Gruppen

Folgend kann nachgeschaut werden, welche Gruppen ein Nutzer tatsächlich hat:

* Bash

````bash
ldapsearch -x \
  -H ldap://10.112.0.4 \
  -D "support.minova@aadds.skytanking.com" \
  -w mBMLkyV10WYHUQF09zNx \
  -b "ou=aaddc users,dc=aadds,dc=skytanking,dc=com" \
  -s one "(objectClass=*)" cn 
````

* CMD

Dabei sind die Global Group memberships Namen erfahrungsgemäß hinten abgeschnitten und somit nicht vollständig:

````cmd
net user support.minova /domain
````

* Active Directory Explorer v1.52

Das [Programm](https://learn.microsoft.com/en-us/sysinternals/downloads/adexplorer) herunterladen und
über den LDAP-Server die Gruppen der Nutzer finden.
In dem Feld `connect to` darf das Protokoll `ldap[s]://` am Anfang nicht angegeben werden.
In dem Feld User sollte die Domain (= @-Teil) weggelassen werden.