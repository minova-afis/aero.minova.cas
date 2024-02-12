# Cas User Rechtsvergabe über WFC

Im Cas gibt es die Möglichkeit jeden Benutzer einer oder mehere Benutzergruppen zuzuteilen.
Jede Benutzergruppen Verfügt über bestimmte Rechte, der benutzer erhält die Rechte jeder Benutzergruppen von der er ein Teil ist.

## Benutzer Anlegen

Wenn man einem Benutzer mit Bestimmten Rechten erstellen will muss man die Maske Verwaltung DB User im CAS Menü öffnen.

<img src="./images/UserRights/image0.png" alt="Alt text" width="25%">

Hier muss man eine ID und ein Passwort festlegen.

<img src="./images/UserRights/image1.png" alt="Alt text" width="40%">

Danach sollte man überprüfen ob es Bereits eine Benutzergruppe mit den gewünschten Rechten gibt und wenn nicht diese Erstellen und zum Schluss dem Benutzer Zuweisen.

<img src="./images/UserRights/image3.png" alt="Alt text" width="25%">

## Benutzergruppe Anlegen

Zum erstellen einer Benutzergruppe muss man im Menü CAS die Maske Benutzergruppe öffnen.

<img src="./images/UserRights/image4.png" alt="Alt text" width="25%">

Danach muss einen Matchcode eingegeben werden und ein Token der mit # beginnt. 

Als Nächstes muss man die Rechte zuweisen.

## Rechte zuweisen

Um Rechte einer Benutzergruppe zuzuweisen muss man in der Maske Benutzergruppe mi Grid Verwaltung Gruppenrechte die Views, Prozeduren und Tabellen Eintragen auf die Benutzergruppe Zugriff haben soll. Hier Kann auch die 
[Reihensicherung](https://github.com/minova-afis/aero.minova.cas/blob/main/service/doc/adoc/security.adoc#tabellenzugriffserlaubnis:~:text=Methoden%20weiter%20gereicht.-,Row%2DLevel%2DSecurity,-Da%20jeder%20User) einschalten.

<img src="./images/UserRights/image5.png" alt="Alt text" width="25%">

## Menü Verwaltung

<img src="./images/UserRights/image6.png" alt="Alt text" width="25%">

In der Menüdefinitions Maske können die Zugriffen auf Menüs und Masken Über [Security Tokens](https://github.com/minova-afis/aero.minova.cas/blob/main/service/doc/adoc/security.adoc#securitytoken:~:text=vom%20CAS%20haben.-,SecurityToken,-Jeder%20User%20erh%C3%A4lt) verwaltet werden. Indem man die Security Token's der Benutzergruppe einträgt.

<img src="./images/UserRights/image7.png" alt="Alt text" width="60%">

