# Changelog
All wesentlichen Änderungen für dieses Projekt werden hier dokumentiert.

Das Format basiert auf [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
## [12.24.1] - 2021-10-25
Setup-Fehler beheben.
## [12.24.0] - 2021-10-13
* #149: Der Nutzer von Prozedur-Aufrufen über `data/procedure` wird im SQL-Session-Context `casUser` abgelegt
  und kann mit der Funktion `dbo.xfCasUser()` ermittelt werden.
  Der Nutzer der SQL-Session kann nicht genutzt werden, da dies immer der SQL-Nutzer des CAS-Dienstes ist.

## [12.21.28] - 2021-09-17

* Installierbares Docker-Image erstellen.
* Vorherige Versionen sind hier nicht dokumentiert.
