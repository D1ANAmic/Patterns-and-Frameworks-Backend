# Beschreibung

Spring Boot Backend für das
Semesterprojekt https://github.com/wernerbreitenstein/Patterns-and-Frameworks

# Produktivsystem

Die Backend Applikation ist auf einem Server deployed und läuft in Docker 
Containern (siehe `src/docker-compose.yml`). Für die lokale Entwicklung 
müssen die `application.properties` entsprechend angepasst werden. 

# Development

## Voraussetzungen

PostgreSQL und JDK müssen lokal installiert sein.

## Application Properties

Für die Konfiguration orientiert sich die Anwendung an den Eigenschaften, die in
`src/main/resources/application.properties` spezifiziert sind.

Um die Anwendung lokal zu starten, muss also zunächst das
Template `/src/main/resources/application.properties.template`
zu `application.properties` umbenannt werden. Achtung: beim lokalen Testen muss
die default `application.properties`
angepasst werden und `localhost` anstelle von `postgres_container` angegeben
werden.

Im Template sind Datenbank und User per default als "frisbee" definiert. Aus
Sicherheitsgründen sollten diese bei einer lokalen Installation umbenannt
werden. Die festgelegten Credentials müssen mit denen, die beim Erstellen der
Datanbank angegeben werden, übereinstimmen.

## Datenbank

###  Datenbankmanagementsystem

PostgreSQL

### Setup Datenbank

(Im Folgenden ggf. "frisbee" mit den in `application.properties` festgelegten
Bezeichnern ersetzen)

Lokalen User erstellen:

```
$ sudo -u postgres createuser --interactive --password frisbee
Shall the new role be a superuser? (y/n) n
Shall the new role be allowed to create databases? (y/n) y
Shall the new role be allowed to create more new roles? (y/n) n
Password: frisbee
```

Datenbank erstellen:

`$ sudo -u postgres createdb frisbee -O frisbee`

Anschließend Postgres neu starten.

### Datenbankmigrationen

Für die Migrationen wird Flyway genutzt. Migrationen können über Intellij
einfach mit dem Plugin "JPA Buddy" erstellt werden,
siehe https://www.baeldung.com/database-migrations-with-flyway#generate-versioned-migrations-in-intellij-idea
.

## Anwendung starten

Das Backend kann über die Klasse `BackendApplication` gestartet werden.

Die App läuft auf Port 8080 und kann unter

http://localhost:8080/api/

im Browser aufgerufen werden.

## Dokumentation
Die Dokumentation wird mit `mvn javadoc:javadoc` erstellt. Sie kann dann
unter `documentation/apidocs/index.html` aufgerufen werden.
## Architekturentscheidungen

Um den Zugriff auf die Datenbank so einfach wie möglich zu gestalten und die Datenpersistierung zu garantieren, haben wir uns im Backend für eine Spring-Boot-Anwendung mit integrierter JPA-Dependency entschieden.

Die Architektur des Backends ist als 3-Tier-Modell konstruiert:
* Die Data-Access-Layer greift auf die Datenbank zu
* Die Service-Layer kümmert sich um die Business-Logik
* Die API-Layer stellt Endpunkte für den Zugriff durch die Clients zur Verfügung

## Frameworks und Libraries

Für das Backend verwenden wir Spring Boot mit folgenden Dependencies:

* PostgreSQL Driver
* Spring Data JPA
* Spring Web
* Lombok
* Spring Security
* Flyway
* Springdoc + Swagger UI

## Client-Server-Communication

Das Frisbee Backend stellt sowohl eine REST API für HTTP Requests als auch einen Socket für bi-direktionale Verbindungen in Echtzeit zur Verfügung.

###REST API

Die REST API funktioniert als Schnittstelle, durch welche der Client auf die in der Datenbank gespeicherten Spieldaten Zugriff erhält. Die Endpunkte können nach Manipulation und Abfragen von Player- und Team-Daten unterschieden werden.

####<u>Player</u>

**/api/players**

* *Beschreibung*: Gibt alle registrierten Spieler als Liste zurück
* *Request Type*: `GET`
* *Response Object*: Liste von Spieler-Objekten `List<Player>`
* *HTTP Status*: 
  * `OK` - falls Anfrage erfolgreich
  * `NO_CONTENT` - falls keine Player-Objekte in der Datenbank vorhanden
  
**/api/players/register**

* *Beschreibung*: Registriert einen Spieler
* *Request Type*: `POST`
* *Request Body*: Spieler-Objekt `Player`
* *Response Object*: Registrierter Spieler `Player`
* *HTTP Status*:
    * `CREATED` - falls erfolgreich in Datenbank gespeichert
    * `BAD_REQUEST` - falls Spieler bereits existiert 

**/api/players/login**

* *Beschreibung*: Meldet einen Spieler an
* *Request Type*: `POST`
* *Request Body*: Spieler Email und Passwort `Map<String, String>`
* *Response Object*: Registrierter Spieler `Player`
* *HTTP Status*:
    * `OK` - falls erfolgreiche Anmeldung
    * `BAD_REQUEST` - falls das falsche Passwort eingegeben wurde
    * `NOT_FOUND` - falls Spieler mit diesen Anmeldedaten nicht existiert

**/api/players/delete-all**

* *Beschreibung*: Löscht alle registrierten Spieler
* *Request Type*: `DELETE`
* *Response Object*: - 
* *HTTP Status*:
    * `OK` - bei erfolgreicher Löschung

**/players/update-player-name/{email}**

* *Beschreibung*: Aktualisiert den Spielernamen
* *Request Type*: `PUT`
* *Path Variable*: Email `String`
* *Request Body*: Neuer Name `String` 
* *Response Object*: Aktualisierter Spieler  `Player`
* *HTTP Status*:
    * `OK` - falls erfolgreiche Aktualisierung
    * `BAD_REQUEST` - falls neuer Name dem alten entspricht oder leere Zeichenkette


####<u>Team</u>

**/api/teams**

* *Beschreibung*: Gibt alle Teams als Liste zurück
* *Request Type*: `GET`
* *Response Object*: Liste von Team-Objekten `List<Team>`
* *HTTP Status*:
    * `OK` - falls Anfrage erfolgreich
    * `NO_CONTENT` - falls keine Teams in der Datenbank vorhanden
    
**/api/teams/{name}**

* *Beschreibung*: Gibt das Team Objekt anhand des Namens zurück
* *Request Type*: `GET`
* *Path Variable*: Name `String`
* *Response Object*: Team-Objekt `Team`
* *HTTP Status*:
    * `OK` - falls Anfrage erfolgreich
    * `NOT_FOUND` - falls kein Team mit dem Namen existiert

**/teams/player/{email}**

* *Beschreibung*: Gibt alle Teams eines Spielers zurück
* *Request Type*: `GET`
* *Path Variable*: Email `String`
* *Response Object*: Liste von Team-Objekten `Team`
* *HTTP Status*:
    * `OK` - falls Anfrage erfolgreich
    * `NOT_FOUND` - falls kein Team mit der Email existiert

**/api/teams/{email}/active**

* *Beschreibung*: Gibt alle aktiven Teams eines Spielers zurück
* *Request Type*: `GET`
* *Path Variable*: Email `String`
* *Response Object*: Liste von Team-Objekten `List<Team>`
* *HTTP Status*:
    * `OK` - falls Anfrage erfolgreich
    * `NOT_FOUND` - falls Spieler mit der Email nicht existiert oder keine aktiven Teams besitzt

**/api/teams/create**

* *Beschreibung*: Erstellt ein Team
* *Request Type*: `POST`
* *Request Body*: Team-Name`String`
* *Response Object*: Erstelltes Team `Team`
* *HTTP Status*:
    * `CREATED` - falls erfolgreich erstellt
    * `BAD_REQUEST` - falls Team bereits existiert

**/api/teams/join**

* *Beschreibung*: Ordnet Spieler einem Team zu
* *Request Type*: `POST`
* *Request Body*: Team-Name und Spieler-Email als `ObjectNode`
* *Response Object*: Team-Objekt `Team`
* *HTTP Status*:
    * `CREATED` - falls erfolgreich erstellt
    * `BAD_REQUEST` - falls Team bereits voll ist oder Spieler schon im Team ist

**/api/teams/update**

* *Beschreibung*: Aktualisiert die Team-Eigenschaften
* *Request Type*: `PUT`
* *Request Body*: Team-Name, Level, Punktestand, Leben und Aktivitätsstatus als `ObjectNode`
* *Response Object*: Team-Objekt `Team`
* *HTTP Status*:
    * `CREATED` - falls erfolgreich aktualisiert
    * `NOT_FOUND` - falls Team nicht existiert

###Socket

Die Echtzeit-Kommunikation zwischen Client und Server erfolgt über Sockets. Sie erfüllt den Zweck, die Spielansicht beider Clients synchron zu halten.

Als Protokoll wird ein SocketRequest-Objekt benutzt, welches aus einem SocketRequestType und einem zugeordneten Wert besteht. Dieses wird vor der Übertragung zu einem String konvertiert. Es existieren sechs verschiedene SocketRequest-Typen:

**INIT**
* *Beschreibung*: Ordnet serverseitig den Client-Thread anhand dessen Team in einen Pool von verbundenen Clients ein und informiert den Client des zweiten Spielers, ob ein Spiel begonnen werden kann.
* *Payload*:
  * Team-Name

**READY**
* *Beschreibung*: Informiert einen Client darüber, ob der jeweils andere Spieler anwesend und bereit zum Spielen ist.
* *Payload*:
  * `true` - falls der zweite Spieler sich auf dem Wartebildschirm eingefunden hat und das Spiel begonnen werden kann
  * `false` - falls sich der zweite Spieler noch nicht auf dem Wartebildschirm eingefunden oder abgemeldet hat

**GAME_RUNNING**
* *Beschreibung*: Informiert den zweiten Client über den Spielstatus des ersten Clients
* *Payload*:
  * Spielstatus (`START`, `PAUSE`, `RESUME`, `CONTINUE`)

**MOVE**
* *Beschreibung*: Synchronisiert die Bewegungen der Spieler für beide Clients
* *Payload*:
  * Bewegungsrichtung eines Spielers (`UP`, `LEFT`, `RIGHT`)

**THROW**
* *Beschreibung*: Synchronisiert die Frisbee-Position beider Clients
* *Payload*:
  * Frisbee-Geschwindigkeit in X und Y Richtung

**DISCONNECT**
* *Beschreibung*: Bricht die Verbindung eines Clients ab, löscht den Client-Thread aus dem Pool der verbundenen Clients und informiert den Client des zweiten Spielers.
* *Payload*:
  * true
