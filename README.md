# Beschreibung Patterns-and-Frameworks

Spring Boot Backend für das
Semesterprojekt https://github.com/wernerbreitenstein/Patterns-and-Frameworks

# Development

## Voraussetungen

PostgreSQL muss lokal installiert sein.

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

## Setup Datenbank

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

Anschließend Postgres neustarten.

### Datenbankmigrationen

Für die Migrationen wird Flyway genutzt. Migrationen können über Intellij
einfach mit dem Plugin "JPA Buddy" erstellt werden,
siehe https://www.baeldung.com/database-migrations-with-flyway#generate-versioned-migrations-in-intellij-idea
.

## Anwendung starten

Das Backend kann über die Klasse `BackendApplication` gestartet werden.

Die App läuft auf Port 8080 unter den im PlayerController festgelegten Routen:

Vorerst:
http://localhost:8080/api/players und http://localhost:8080/api/players/{id}

## Dokumentation
Die Dokumentation wird mit `mvn javadoc:javadoc` erstellt. Sie kann dann
unter `documentation/apidocs/index.html` aufgerufen werden.

## Frameworks und libraries

Für das Backend verwenden wir Spring Boot mit folgenden Dependencies:

* PostgreSQL Driver
* Spring Data JPA
* Spring Web
* Lombok

## Datenbank

PostgreSQL: https://www.postgresql.org

# API Dokumentation

Nach dem Start der Applikation ist die Dokumentation der Endpunkte über eine 
Swagger UI unter `localhost:8080/swagger-ui/index.html` erreichbar. 

Die Dokumentation als JSON ist unter `http://localhost:8080/api-docs` 
erreichbar oder kann direkt in `documentation/swagger/swagger.json` 
eingesehen werden.
