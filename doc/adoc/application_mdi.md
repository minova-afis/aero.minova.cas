# Generated MDI
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
    - Menu - Menustruktureinträge
    - Toolbar
- Menu: Menu, in welchem die Maske eingetragen wird
- Position: Position der Form/Maske innerhalb des Menus, kann auch mit Fließkommawert angegeben werden
- SecurityToken: 
- LastAction:
- LastDate:
- LastUser: 
