# Esporre Api REST code first utilizzando Spring Boot

Questo tutorial mostra come esporre api REST in modalità code first utilizzando Spring Boot.

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/esporre-api-rest-code-first-spring-boot)

## Prerequisiti

- Java 17
- Maven
- Spring Boot 2.7.11

## Aggiungere Spring Boot nella sezione di dependency management al pom.xml

Per la gestione delle dipendenze Spring Boot bisogna aggiungere nel file **pom.xml** la dipendenza **spring-boot-starter-parent** nella sezione di **dependencyManagement**.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.7.11</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Aggiungere le dipendenze necessarie al pom.xml

Per l'esposizione delle Api REST bisogna bisogna aggiungere nel file **pom.xml** le dipendenze **spring-boot-starter-web** e **swagger-annotations**.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>2.2.8</version>
</dependency>
```

## Creare una classe Main

Per l'utilizzo dell'applicazione bisogna creare una classe contenente un metodo **main** annotata con l'annotation **org.springframework.boot.SpringApplication**.

L'istruzione necessaria allo start dell'applicazione Spring Boot è **SpringApplication.run(Application.class, args)**.

```java
package example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        log.info("Start Application");

        SpringApplication.run(Application.class, args);
    }
}
```

## Creare un apposito @Controller per l'esposizione delle Api REST

In questo esempio si espone un'api REST con le funzionalità:
1. **GET /Anagrafiche (metodo getAnagrafiche)**: per il recupero di tutte le anagrafiche
2. **GET /anagrafiche/{idAnagrafica} (metodo getAnagrafica)**: per il recupero di un'anagrafica a partire dall'id
3. **POST /anagrafiche (metodo postAnagrafica)**: per la creazione di una nuova anagrafica

Nell'esempio in basso vengono utilizzate:
- L'annotation **@RequestMapping("/api/v1")** per specificare che il base path dell'api REST è **/api/v1**, questo vuol dire che tutte le api REST definite nella classe saranno esposte in **http://host:port/api/v1**
- L'annotation **@RequestMapping** per indicare in che modalità e sotto quale path esporre l'api REST

```java
package example;

import io.swagger.v3.oas.annotations.Parameter;
import it.leogioia.model.Anagrafica;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/anagrafiche",
            produces = {"application/json"}
    )
    public ResponseEntity<List<Anagrafica>> getAnagrafiche() {
        log.info("Invocazione getAnagrafiche");
        return new ResponseEntity<List<Anagrafica>>(anagraficheManager.getAnagrafiche(), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/anagrafiche/{idAnagrafica}",
            produces = {"application/json"}
    )
    public ResponseEntity<Anagrafica> getAnagrafica(@Parameter(required = true) @PathVariable(name = "idAnagrafica") String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.getAnagrafica(idAnagrafica), HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/anagrafiche",
            produces = {"application/json"}
    )
    public ResponseEntity<Anagrafica> postAnagrafica(@Parameter(required = true) @RequestBody Anagrafica anagrafica) {
        log.info("Invocazione postAnagrafica({})", anagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.putAnagrafica(anagrafica), HttpStatus.OK);
    }
}
```

## Compilare l'applicazione

Per compilare l'applicazione bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando:

```bash
mvn clean package
```

## Eseguire l'applicazione

Per eseguire l'applicazione bisogna spostarsi nella cartella **target** ed eseguire il seguente comando:

```bash
java -jar mia-applicazione.jar
```

## Invocare le Api REST

Per invocare le Api REST bisogna eseguire le seguenti curl:

1. Recupero di tutte le anagrafiche:
```bash
curl -XGET "http://localhost:8080/api/v1/anagrafiche"
```
2. Recupero di un'anagrafica a partire dall'id:
```bash
curl -XGET "http://localhost:8080/api/v1/anagrafiche/2"
```
3. Creazione di una nuova anagrafica:
```bash
curl -XPOST --header "Content-Type: application/json" --data '{"id":"2","nome":"Mario","cognome":"Rossi","indirizzo":"Via Roma, 54"}' "http://localhost:8080/api/v1/anagrafiche"
```