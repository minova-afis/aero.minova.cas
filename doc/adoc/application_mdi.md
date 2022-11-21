# Generated MDI (Menu Definition Interface)
Im Folgenden wir die Generierung der MDI beschrieben. Die MDI soll vom CAS generiert werden. Dafür wird eine SQL-Tabelle ausgelesen.
Diese hat folgenden Aufbau

| KeyLong | ID | Typ | Icon | Menu | Position | SecurityToken | LastAction | LastDate | LastUser |
|---------|----|-----|------|------|----------|---------------|------------|----------|----------|
|         |    |     |      |      |          |               |            |          |          |

- KeyLong: Identity-Spalte
- ID: eindeutiger Text innerhalb der MDI Datei
- Icon: Icon, welches auch in dem Menu oder in der Toolbar angezeigt wird.
- Typ: Beschreibt den Typ für den entsprechenden Eintrag
    - Application - Eintrag darf nur einmal in der Tabell existieren, (SIS, DISPO..)
    - From - Action Einträge für die einzelnen XML-Masken
    - Menu - Menustruktureinträge, kann auch geschachtelt werden
- Menu: Menupunkt, in welchem die Maske eingetragen wird
- Position: Position der Form/Maske innerhalb des Menus, kann auch mit Fließkommawert angegeben werden
- SecurityToken: 
- LastAction:
- LastDate:
- LastUser: 

## Beispiele

| KeyLong | ID | Text | Typ | Icon | Menu | Position | SecurityToken | LastAction | LastDate | LastUser |
|---------|----|----|-----|------|------|----------|---------------|------------|----------|----------|
| 1 | driver |@Driver.Administration | Form | Driver |MasterData | 1.2| zdga6737er87gd6zed | 2 | 21.11.2022 12:23:45 |erlanger |
| 2 | masterdata |@menu.masterdata | Menu| | | 1| h8h7d349hd7e8 | 2 | 21.11.2022 12:34:12 |erlanger |
| 3 | item |@Item.Administration | Form | Item |MasterData | 3| sdferf34534rf4r4 | 2 | 21.11.2022 12:23:45 |erlanger |
| 4 | vehicle |@Vehicle.Administration | Form | Truck |MasterData |5| fsfgerg5t45g4trf3 | 2 | 21.11.2022 12:23:45 |erlanger |
| 5 | sis |@sis.Title | Application | | | | sdhgwg67438fg8r | 2 | 21.11.2022 12:23:45 |erlanger |

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
