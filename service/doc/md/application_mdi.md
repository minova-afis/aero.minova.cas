# Generated MDI (Menu Definition Interface)
Im Folgenden wir die Generierung der MDI beschrieben. Die MDI soll vom CAS generiert werden. Dafür wird eine SQL-Tabelle ausgelesen.

- KeyLong: Identity-Spalte
- ID: eindeutiger Text innerhalb der MDI Datei
- Icon: Icon, welches auch in dem Menu oder in der Toolbar angezeigt wird.
- Label: Text, der über die Übersetzungen eingetragen wird
- Typ: Beschreibt den Typ für den entsprechenden Eintrag
    - Application - Eintrag darf nur einmal in der Tabell existieren, (SIS, DISPO..)
    - From - Action Einträge für die einzelnen XML-Masken
    - Menu - Menustruktureinträge, kann auch geschachtelt werden
- Menu: Menupunkt, in welchem die Maske eingetragen wird
- Position: Position der Form/Maske innerhalb des Menus, kann auch mit Fließkommawert angegeben werden
- ModulName: Der Name des Moduls/Projekts, das den Eintrag erstellt/geändert hat. Damit wir nachvollziehen können, wo die Zeile her kommt. Wird beim Generieren nicht verwendet
- SecurityToken: eindeutiger Benutzer-Token
- LastAction: Status des Datensatzes, -1 (gelöscht), 1 (neu erstellt) , 2 (upgedated)
- LastDate: Datum der letzten Änderung
- LastUser: Benutzer, der zuletzt dieses Datensatz angepasst hat.

## Beispiele

| KeyLong | ID | Label | Typ | Icon | Menu | Position | ModulName | SecurityToken | LastAction | LastDate | LastUser |
|---------|----|----|-----|------|------|----------|---------------|---------------|------------|----------|----------|
| 1 | driver |@Driver.Administration | Form | Driver |MasterData | 1.2|aero.minova.driver | admin | 2 | 21.11.2022 12:23:45 |erlanger |
| 2 | masterdata |@menu.masterdata | Menu| | | 1|aero.minova.cas| admin | 2 | 21.11.2022 12:34:12 |erlanger |
| 3 | item |@Item.Administration | Form | Item |MasterData | 3|aero.minova.item| verwaltung | 2 | 21.11.2022 12:23:45 |erlanger |
| 4 | vehicle |@Vehicle.Administration | Form | Truck |MasterData |5|aero.minova.vehicle| verwaltung | 2 | 21.11.2022 12:23:45 |erlanger |
| 5 | sis |@sis.Title | Application | | | |aero.minova.sis| verwaltung | 2 | 21.11.2022 12:23:45 |erlanger |

Aus dem oberen Beispiel sollte die folgende MDI Datei erstellt werden.


```
<?xml version="1.0" encoding="UTF-8"?>
<main icon="SIS.ico" titel="@sis.Title">
	<action action="Item.xml" generic="true" icon="Item" id="item" text="@Item.Administration"/>
	<action action="Vehicle.xml" generic="true" icon="Truck" id="vehicle" text="@Vehicle.Administration"/>
	<action action="Driver.xml" generic="true" icon="Driver" id="driver" text="@Driver.Administration"/>
	<menu id="main">
	    <menu id="masterdata" text="@menu.masterdata" position="1" override="false">
		<entry id="driver" position="1.2" override="false" type="action"/>
		<entry id="item" position="3" override="false" type="action"/>
		<entry id="vehicle" position="5" override="false" type="action"/>
        </menu>
    </menu>
</main>

```

Zum Erstellen eines Mdi-Skriptes kann man sich an der [initMdi.sql](./../../../app/src/main/app/sql/initMdi.sql) des cas.app-Projektes orientieren.

Zu beachten ist außerdem, dass das Skript in der Setup.xml eingetragen werden muss **UND** es einen anderen Namen haben muss, als 'initMdi.sql', da dieser Name ja bereits vom CAS verwendet wird. 

# Wichtig!

Damit das Filtern nach SecurityToken auch funktioniert, muss in der xtcasLuUserPrivilegesUserGroup für die entsprechende Gruppe das RowLevelSecurity-Bit auf true gesetzt werden.

Es können in der SecurityToken-Spalte immer nur ein SecurityToken eingefügt werden!
Wenn mehrere Gruppen eine Maske über den SecurityToken erhalten sollen, muss eine Übergruppe erstellt werden, in welcher die entsprechenden Benutzer enthalten sind.

