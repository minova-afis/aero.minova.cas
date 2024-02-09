# Rechteverwaltung CAS-WFC über die SQL-Datenbank

-- Inhalt der Doku, Problembeschreibung + Problemlösung

Um in der Datenbank direkt die Rechte zuverwalten ist es erstaml Notwendig sich mit der richtigen Datenbank zu Verbinden.
Der erste Schritt, das Anlegen der Benutzer ist in der Anwendung selber zu machen. Das liegt daran das das Passwort in der anwendung zu einem langen String wird sodass dieses nicht ausgelesen werden kann.
Die **ID** kann hier Frei gewählt werden, genauso wie das **Password**

![Alt text](../adoc/images/SqlInsertCasUser.png)

-- Aufbau der Berechrtigungen in der Datenbank. Benutzer, Benutzergruppen, Rechte
Jeder Benutzer kann einer Gruppe zuhgeordnet werden, diese kann dann bestimmte Rechte beinhalten. Ein Benutzer kann auch Teil von mehreren Gruppen sein: Beispiel: ---


<img width="818" alt="image" src="https://github.com/minova-afis/aero.minova.cas/assets/20420898/61e15ef8-2e1d-4888-8231-d1621fc9d267">


Als nächstes sollte man eine Benutzergruppe anlegen. Diese kann beliebig heißen, benötigt werden ein *KeyText* und ein **SecurityToken**. Beim **SecurityToken** ist es wichtig das ein **'#'** davor steht:

![image::images/SqlInsertCasUserGroup.png\[\]](../adoc/images/SqlInsertCasUserGroup.png)

Als kleiner Zwischenschritt navigiert man nun zu *xtcasMdi* und ändert dort den **SecurityToken** zum gleichen wie bei **xtcasUserGroup**. Das **'#'** wird hierbei nicht benötigt. Diese änderung ist wichtig damit die jeweilige Benutzergruppe auch die Maske sehen kann. 

![image::images/SqlInsertMdi.png\[\]](../adoc/images/SqlInsertMdi.png)

Danach muss nurnoch der jeweilige Benutzer zu der Benutzergruppe hinzugefügt werden. Dies macht man indem man die **xtcasAuthorities** tabelle abfragt, und dort dann den jeweiligen **UserName** und die Gruppe welche dieser zugefügt werden soll, also der *Authority* einträgt:

![image::images/SqlInsertAuthority.png\[\]](../adoc/images/SqlInsertAuthority.png)

Der Letzte Schritt, ist das man den Benutzergruppen Prozeduren zuschreibt, welche dieser Benutzen darf. Es ist sehr wichtig das Überall die **Rowlevelsecurity*** auf **0** ist außer bei **xpcasInitMdi** dort muss diese unbedingt auf **1** gestellt sein:

![image::images/SqlInsertPrivilege.png\[\]](../adoc/images/SqlInsertPrivilege.png)
