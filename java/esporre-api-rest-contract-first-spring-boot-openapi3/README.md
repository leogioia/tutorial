# Esporre Api REST contract first utilizzando Spring Boot e Openapi3

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/esporre-api-rest-contract-first-spring-boot-openapi3)

## Descrizione

Questo tutorial mostra come esporre api REST in modalità code first utilizzando Spring Boot.

## Prerequisiti

- Java 17
- Maven
- Spring Boot 2.7.11
- Openapi 3

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
4. **jackson-databind-nullable**

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

<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>2.0.1.Final</version>
</dependency>

<dependency>
    <groupId>org.openapitools</groupId>
    <artifactId>jackson-databind-nullable</artifactId>
    <version>0.2.6</version>
</dependency>
```

## Creare un'interfaccia Openapi 3

Per la creazione di un'interfaccia Openapi 3 bisogna creare un file YAML che descriva l'api che si vuole esporre.

Nell'esempio viene creato il file **src/main/resources/interfaces/anagrafica_v1.yml** per l'esposizione delle api REST:
1. **GET /Anagrafiche (metodo getAnagrafiche)**: per il recupero di tutte le anagrafiche
2. **GET /anagrafiche/{idAnagrafica} (metodo getAnagrafica)**: per il recupero di un'anagrafica a partire dall'id
3. **POST /anagrafiche (metodo postAnagrafica)**: per la creazione di una nuova anagrafica


```yaml
openapi: "3.0.0"
info:
  description: 'Anagrafica'
  version: 1.0.0
  title: 'Esempio API Anagrafica'
servers:
  - url: /api/v1
tags:
  - name: Anagrafica

paths:

  '/anagrafiche':
    get:
      tags:
        - "Anagrafica"
      operationId: getAnagrafiche
      responses:
        '200':
          description: "Success"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/ListaAnagrafiche"
        '400':
          description: "Bad Request"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '401':
          description: "Unauthorized"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '500':
          description: "Internal Server Error"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '503':
          description: "Service Unavailable"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"

    post:
      tags:
        - "Anagrafica"
      operationId: postAnagrafica
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/Anagrafica'
      responses:
        '200':
          description: "Success"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Anagrafica"
        '400':
          description: "Bad Request"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '401':
          description: "Unauthorized"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '500':
          description: "Internal Server Error"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '503':
          description: "Service Unavailable"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"

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
          schema:
            type: string
      responses:
        '200':
          description: "Success"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Anagrafica"
        '400':
          description: "Bad Request"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '401':
          description: "Unauthorized"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '500':
          description: "Internal Server Error"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
        '503':
          description: "Service Unavailable"
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"

components:

  schemas:

    ListaAnagrafiche:
      type: array
      items:
        $ref: "#/components/schemas/Anagrafica"

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
    <groupId>org.openapitools</groupId>
    <artifactId>openapi-generator-maven-plugin</artifactId>
    <version>6.2.1</version>
    <executions>
        <execution>
            <id>generate-stub-server-anagrafica_v1</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <inputSpec>
                    ${project.basedir}/src/main/resources/interfaces/anagrafica_v1.yml
                </inputSpec>
                <generatorName>spring</generatorName>
                <output>${project.build.directory}/generated-sources</output>
                <configOptions>
                    <apiPackage>anagrafica_v1.api</apiPackage>
                    <modelPackage>anagrafica_v1.model</modelPackage>
                    <invokerPackage>anagrafica_v1.invoker</invokerPackage>
                    <interfaceOnly>true</interfaceOnly>
                    <dateLibrary>java</dateLibrary>
                </configOptions>
                <generateApiTests>false</generateApiTests>
                <generateApiDocumentation>false</generateApiDocumentation>
                <generateModelTests>false</generateModelTests>
                <generateModelDocumentation>false</generateModelDocumentation>
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
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApiImpl implements AnagraficheApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    public ResponseEntity<List<Anagrafica>> getAnagrafiche() {
        log.info("Invocazione getAnagrafiche");
        return new ResponseEntity<List<Anagrafica>>(anagraficheManager.getAnagrafiche(), HttpStatus.OK);
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