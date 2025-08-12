<a href="https://www.minova.de/" >
<img src="https://www.minova.de/files/Minova/Ueber_uns/minova-logo-105.svg" alt="logo" align="right"/>
</a>

# CAS - Core Application System

<p align="left">
  <a href="https://hub.docker.com/r/minova/aero.minova.cas.app/tags?page=1&ordering=last_updated">
    <img alt="Docker Images" src="https://img.shields.io/badge/Docker%20images-blue">
  </a>
  <img src="https://img.shields.io/badge/license-EPL%202.0-green">
  <img src="https://github.com/minova-afis/aero.minova.cas/actions/workflows/continuous-integration.yml/badge.svg">
</p>


# Architektur-Übersicht

<figure>
<img src="doc/cas-overview.svg" alt="cas overview" />
</figure>

<img style="width: 100%" src="doc/konzept-von-free-tables-fuer-das-freistellungsportal.png">

# Beschreibung

Stellt eine einfache Schnittstelle zu Daten und Prozeduren einer SQL-Datenbank, Erweiterungen basierend auf Java, sowie einem Read-Only-Dateisystem bereit.
Der Fokus liegt hierbei darauf, das Backend und die Infrastruktur für Eclipse/[WFC](https://github.com/minova-afis/aero.minova.rcp)-Anwendungen bereitzustellen.

Eine Illustration der Struktur wird [hier](./doc/adoc/structure.adoc) bereitgestellt.

Für die alten Hasen unter uns: ein sehr großer Teil vom CAS stellt eine REST-Schnittstelle zu den SQL-Funktionalitäten vom Ncore und dem Install-Tool bereit.

# Status

Wir sind jetzt an einem Punkt angekommen,
wo Breaking-Changes bzgl. der REST-Schnittstelle und den Klassen `aero.minova.cas.[controller. *, service. *, servicenotifier. *, sql. *, CustomLogger]`
nicht erwünscht und so weit, wie sinnvoll möglich, vermieden werden.

Clients und Extensions sollen sich also darauf verlassen können,
dass die API möglichst stabil gehalten wird.

# Weiterführende Dokumentation

-   [Symptome, deren Ursachen und Lösungen](./doc/adoc/support.adoc)

-   [Konzept von Free Tables für das Freistellungsportal](./doc/docx/konzept-von-free-tables-fuer-das-freistellungsportal.docx) ([PDF](./doc/pdf/konzept-von-free-tables-fuer-das-freistellungsportal.pdf))

-   [Changelog](./CHANGELOG.md)

-   [CAS API](./api/doc/adoc/index.adoc)

-   [CAS-Dienst](./service/doc/adoc/index.adoc)

-   [Installation](./service/doc/adoc/installation.adoc)

-   [Property Übersicht](./service/doc/adoc/properties.adoc)

-   [How to Extensions und App-Projekte](./service/doc/adoc/extensions.adoc)

-   [Projektstruktur](./doc/adoc/projectStructure.adoc)

-   [Strukturübersicht](./doc/adoc/structure.adoc)

-   [Tabellen-Index](./app/doc/adoc/table-index.adoc)
