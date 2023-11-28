# Esporre Api REST contract first utilizzando Spring Boot e Swagger2

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/esporre-api-rest-contract-first-spring-boot-swagger2)

## Descrizione

Questo tutorial mostra come esporre api REST in modalità contract first utilizzando Spring Boot e Swagger2.

## Prerequisiti

- Java 17
- Maven
- Spring Boot 2.7.11
- Swagger 2

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

Per l'esposizione delle Api REST bisogna bisogna aggiungere nel file **pom.xml** le dipendenze:
1. **spring-boot-starter-web**
2. **swagger-annotations**
3. **validation-api**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>1.6.4</version>
</dependency>

<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>
```

## Creare un'interfaccia Swagger 2

Per la creazione di un'interfaccia Swagger 2 bisogna creare un file YAML che descriva l'api che si vuole esporre.

Nell'esempio viene creato il file **src/main/resources/interfaces/anagrafica_v1.yml** per l'esposizione delle api REST:
1. **GET /Anagrafiche (metodo getAnagrafiche)**: per il recupero di tutte le anagrafiche
2. **GET /anagrafiche/{idAnagrafica} (metodo getAnagrafica)**: per il recupero di un'anagrafica a partire dall'id
3. **POST /anagrafiche (metodo postAnagrafica)**: per la creazione di una nuova anagrafica


```yaml
swagger: '2.0'
info:
  description: 'Anagrafica'
  version: 1.0.0
  title: 'Esempio API Anagrafica'
basePath: /api/v1
tags:
  - name: Anagrafica

paths:

  '/anagrafiche':
    get:
      tags:
        - "Anagrafica"
      operationId: getAnagrafiche
      produces:
        - "application/json"
      responses:
        '200':
          description: "Success"
          schema:
            $ref: "#/definitions/ListaAnagrafiche"
        '400':
          description: "Bad Request"
          schema:
            $ref: "#/definitions/Error"
        '401':
          description: "Unauthorized"
          schema:
            $ref: "#/definitions/Error"
        '500':
          description: "Internal Server Error"
          schema:
            $ref: "#/definitions/Error"
        '503':
          description: "Service Unavailable"
          schema:
            $ref: "#/definitions/Error"

    post:
      tags:
        - "Anagrafica"
      operationId: postAnagrafica
      parameters:
        - in: body
          name: body
          required: true
          schema:
            $ref: '#/definitions/Anagrafica'
      responses:
        '200':
          description: "Success"
          schema:
            $ref: "#/definitions/Anagrafica"
        '400':
          description: "Bad Request"
          schema:
            $ref: "#/definitions/Error"
        '401':
          description: "Unauthorized"
          schema:
            $ref: "#/definitions/Error"
        '500':
          description: "Internal Server Error"
          schema:
            $ref: "#/definitions/Error"
        '503':
          description: "Service Unavailable"
          schema:
            $ref: "#/definitions/Error"

  '/anagrafiche/{idAnagrafica}':
    get:
      tags:
        - "Anagrafica"
      operationId: getAnagrafica
      parameters:
        - in: path
          name: idAnagrafica
          required: true
          description: "Identificativo anagrafica"
          type: string
      responses:
        '200':
          description: "Success"
          schema:
            $ref: "#/definitions/Anagrafica"
        '400':
          description: "Bad Request"
          schema:
            $ref: "#/definitions/Error"
        '401':
          description: "Unauthorized"
          schema:
            $ref: "#/definitions/Error"
        '500':
          description: "Internal Server Error"
          schema:
            $ref: "#/definitions/Error"
        '503':
          description: "Service Unavailable"
          schema:
            $ref: "#/definitions/Error"

definitions:

  ListaAnagrafiche:
    type: array
    items:
      $ref: "#/definitions/Anagrafica"

  Anagrafica:
    type: object
    required:
      - id
      - nome
      - cognome
      - indirizzo
    properties:
      id:
        type: string
      nome:
        type: string
      cognome:
        type: string
      indirizzo:
        type: string

  Error:
    type: object
    required:
      - code
      - message
      - layer
      - component
    properties:
      code:
        type: string
      message:
        type: string
      layer:
        type: string
      component:
        type: string
```

## Aggiungere il plugin per la generazione automatica delle interfacce Spring al pom.xml

Per la generazione delle interfacce Spring bisogna aggiungere il plugin **openapi-generator-maven-plugin** al file **pom.xml**.

Questo plugin si occupa della generazione degli stub server (nella directory **target/generated-sources**) da implementare per l'esposizione delle api REST.

```xml
<plugin>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-codegen-maven-plugin</artifactId>
    <version>${swagger-codegen-maven-plugin}</version>
    <executions>
        <execution>
            <id>generate-stub-anagrafica_v1</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>
                    ${project.basedir}/src/main/resources/interfaces/anagrafica_v1.yml
                </inputSpec>
                <output>${project.build.directory}/generated-sources/anagrafica_v1</output>
                <modelPackage>anagrafica_v1.model</modelPackage>
                <apiPackage>anagrafica_v1.api</apiPackage>
                <invokerPackage>anagrafica_v1.invoker</invokerPackage>
                <configOptions>
                    <interfaceOnly>true</interfaceOnly>
                    <dateLibrary>java8</dateLibrary>
                </configOptions>
                <generateApiTests>false</generateApiTests>
                <generateApiDocumentation>false</generateApiDocumentation>
                <generateModelTests>false</generateModelTests>
                <generateModelDocumentation>false</generateModelDocumentation>
                <language>spring</language>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Generare gli stub server Spring Boot

Per generare gli stub server bisogna lanciare il comando:

```bash
mvn clean generate-sources
```

## Creare un @Controller che implementi l'interfaccia Stub

Per esporre le api REST bisogna creare una classe annotata con **@Controller** che implementi l'interfaccia generata automaticamente.

**N.B.**: non bisogna aggiungere le annotation sui singoli metodi in quanto vengono ereditate dall'interfaccia che viene implementata.

```java
import anagrafica_v1.api.AnagraficheApi;
import anagrafica_v1.model.Anagrafica;
import anagrafica_v1.model.ListaAnagrafiche;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApiImpl implements AnagraficheApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    public ResponseEntity<ListaAnagrafiche> getAnagrafiche() {
        log.info("Invocazione getAnagrafiche");
        ListaAnagrafiche listaAnagrafiche = new ListaAnagrafiche();
        listaAnagrafiche.addAll(anagraficheManager.getAnagrafiche());
        return new ResponseEntity<>(listaAnagrafiche, HttpStatus.OK);
    }

    public ResponseEntity<Anagrafica> getAnagrafica(String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);
        return new ResponseEntity<Anagrafica>(anagraficheManager.getAnagrafica(idAnagrafica), HttpStatus.OK);
    }

    public ResponseEntity<Anagrafica> postAnagrafica(Anagrafica anagrafica) {
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