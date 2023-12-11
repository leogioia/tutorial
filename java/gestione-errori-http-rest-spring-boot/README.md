# Gestire errori HTTP in api REST esposta tramite Spring Boot

Questo tutorial mostra come gestire gli errori in un'api REST esposta tramite Spring Boot e Openapi3.
Gli errori gestiti vengono in prima battuta dichiaratti nell'interfaccia Openapi3 e successivamente gestiti nell'applicativo.

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/gestione-errori-http-rest-spring-boot)

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

## Creare un Enum per la gestione degli errori

Per gestire gli errori, in questo esempio, è stato creato un Enum che contiene 3 campi:
1. Codice errore
2. Messaggio errore
3. Stato HTTP da restituire

```java
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {

    E01("E01", "Anagrafica non presente", 404);

    private String code;
    private String message;
    private int httpStatus;
}
```

## Creare un'eccezione custom

Per rilanciare errori da un qualsiasi punto dell'applicazione bisogna creare una eccezione custom che estenda **RuntimeException** e contenga al proprio interno un'istanza dell'enum creato precedentemente.

```java
import lombok.Getter;

@Getter
public class ErrorException extends RuntimeException {

    private ErrorEnum errorEnum;

    public ErrorException(ErrorEnum errorEnum) {
        super(errorEnum.getCode() + " - " + errorEnum.getMessage());
        this.errorEnum = errorEnum;
    }
}
```

## Creare un @ControllerAdvice per intercettare e gestire le eccezioni

Per intercettare e gestire le eccezioni rilanciate all'interno dell'applicazione bisogna creare un apposito **@ControllerAdvice** che al proprio interno intercetti e gestisca le eccezioni rilanciate dall'applicazione.

Ogni metodo annotato con **@ExceptionHandler(ClasseEccezione.class)** viene invocato ogni volta che viene rilanciata nel codice un'eccezione del tipo **ClasseEccezione.class**.

```java
import anagrafica_v1.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Error> errorException(ErrorException e) {
        log.info("Managing Error Code: {}, Message: {}, HttpStatus: {}",
                e.getErrorEnum().getCode(),
                e.getErrorEnum().getMessage(),
                e.getErrorEnum().getHttpStatus());

        HttpStatus httpStatus = Optional.of(HttpStatus.valueOf(e.getErrorEnum().getHttpStatus()))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(httpStatus)
                .body(createError(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> errorException(Exception e) {
        log.info("Managing Error {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createError(e.getMessage()));
    }

    private Error createError(ErrorException e) {
        Error error = new Error();
        error.setCode(e.getErrorEnum().getCode());
        error.setMessage(e.getErrorEnum().getMessage());
        return error;
    }

    private Error createError(String message) {
        Error error = new Error();
        error.setCode("500");
        error.setMessage(message);
        return error;
    }
}
```

## Creare un @Controller che restituisca l'errore HTTP appropriato

Per implementare la logica di restituzione del corretto errore HTTP bisogna creare un **@Controller** che rilanci l'eccezione corretta.

In questo esempio viene rilanciata un'eccezione se una data anagrafica richiesta non è presente.

```java
import anagrafica_v1.api.AnagraficheApi;
import anagrafica_v1.model.Anagrafica;
import it.leogioia.error.ErrorException;
import it.leogioia.persistence.AnagraficheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static it.leogioia.error.ErrorEnum.E01;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class AnagraficaApiImpl implements AnagraficheApi {

    @Autowired
    AnagraficheManager anagraficheManager;

    public ResponseEntity<Anagrafica> getAnagrafica(String idAnagrafica) {
        log.info("Invocazione getAnagrafica({})", idAnagrafica);

        Anagrafica anagrafica = Optional.ofNullable(anagraficheManager.getAnagrafica(idAnagrafica))
                .orElseThrow(() -> new ErrorException(E01));

        return new ResponseEntity<>(anagrafica, HttpStatus.OK);
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

Si invocare l'api REST con un idAnagrafica non presente tramite il comando:

```bash
curl -XGET "http://localhost:8080/api/v1/anagrafiche/2"
```

Si può notare che la risposta ha come codice **HTTP 404** e come body:

```json
{
  "code": "E01",
  "message": "Anagrafica non presente"
}
```