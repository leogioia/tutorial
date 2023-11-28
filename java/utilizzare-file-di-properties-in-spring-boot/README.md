# Utilizzare file di properties in Spring Boot

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/utilizzare-file-di-properties-in-spring-boot)

## Descrizione

Questo tutorial mostra come utilizzare un file di properties per esternalizzare configurazioni applicative.

Per l'esempio seguente viene implementata un'api REST che sfrutta l'utilizzo di properties per costruire la risposta.

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

## Creare un @Service per la lettura di properties diverse da semplici String

In questo esempio si utilizzano due properties:
1. **descrizione.offerte**: una semplice _String_
2. **lista.prodotti**: una lista di coppie _String_/_Double_ separate da virgole

Per la lettura ed il corretto parse della property al punto 2 bisogna implementare il seguente **@Service** contenente il metodo **getProdotti** che si occupa della trasformazione della property in una lista di prodotti.

```java
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyService {

    public List<Prodotto> getProdotti(String p) {
        String prodottiString = Optional.ofNullable(p).orElse("");

        return Arrays.stream(prodottiString.split(","))
                .map(pr -> Prodotto.builder()
                        .descrizione(pr.substring(0, pr.indexOf(":")))
                        .prezzo(Double.parseDouble(pr.substring(pr.indexOf(":") + 1)))
                        .build())
                .collect(Collectors.toList());
    }
}
```

## Creare un apposito @Controller per l'esposizione delle Api REST

In questo esempio si espone un'api REST con le funzionalità:
1. **GET /prodotti (metodo getProdotti)**: per il recupero di tutti i prodotti

Nell'esempio in basso vengono utilizzate:
- L'annotation **@Value("${descrizione.offerte}")** per il recupero della property **descrizione.offerte**
- L'annotation **@Value("#{propertyService.getProdotti('${lista.prodotti}')}")** per sfruttare le funzionalità implementate nel **@Service** **PropertyService** per il recupero della lista di prodotti a partire dalla property **lista.prodotti**

```java
import it.leogioia.model.GetProdottiResponse;
import it.leogioia.model.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1")
public class ProdottoApi {

    @Value("${descrizione.offerte}")
    String descrizioneOfferte;
    @Value("#{propertyService.getProdotti('${lista.prodotti}')}")
    List<Prodotto> prodotti;

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/prodotti",
            produces = {"application/json"}
    )
    public ResponseEntity<GetProdottiResponse> getProdotti() {
        log.info("Invocazione getProdotti");

        GetProdottiResponse getProdottiResponse = GetProdottiResponse.builder()
                .descrizioneOfferte(descrizioneOfferte)
                .prodotti(prodotti)
                .build();

        return new ResponseEntity<>(getProdottiResponse, HttpStatus.OK);
    }
}
```

## Creare files di properties

Spring Boot supporta la lettura di un file di properties di default chiamato **application.properties** e situato nella directory **src/main/resources**.

In questo esempio viene creato un secondo file di properties chiamato **application-local.properties** e situato nella stessa directory per sfruttare le funzionalità offerte dai profili Spring.

Il contenuto dei file è mostrato di seguito.

**application.properties**

```properties
descrizione.offerte=Offerte Natale
lista.prodotti=Computer:500,Tablet:300,Smartphone:200
```

**application-local.properties**

```properties
descrizione.offerte=Offerte Black Friday
lista.prodotti=Frullatore:50,Frigorifero:600,Tostapane:20
```

## Compilare l'applicazione

Per compilare l'applicazione bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando:

```bash
mvn clean package
```

## Eseguire l'applicazione

Per eseguire l'applicazione bisogna spostarsi nella cartella **target** ed eseguire:

- Per l'utilizzo del profilo di default (utilizzando quindi il file **application.properties**) il comando:

```bash
java -jar mia-applicazione.jar
```

- Per l'utilizzo del profilo **local** (utilizzando quindi il file **application-local.properties**) il comando:

```bash
java -Dspring.profiles.active=local -jar mia-applicazione.jar
```

## Invocare le Api REST

Per invocare le Api REST bisogna eseguire la seguente curl:

```bash
curl -XGET "http://localhost:8080/api/v1/prodotti"
```

Si può notare che:

1. In caso di utilizzo del profilo di **default**, la risposta ricevuta è:

```json
{
  "descrizioneOfferte": "Offerte Natale",
  "prodotti": [
    {
      "descrizione": "Computer",
      "prezzo": 500.0
    },
    {
      "descrizione": "Tablet",
      "prezzo": 300.0
    },
    {
      "descrizione": "Smartphone",
      "prezzo": 200.0
    }
  ]
}
```

2. In caso di utilizzo del profilo **local**, la risposta ricevuta è:

```json
{
  "descrizioneOfferte": "Offerte Black Friday",
  "prodotti": [
    {
      "descrizione": "Frullatore",
      "prezzo": 50.0
    },
    {
      "descrizione": "Frigorifero",
      "prezzo": 600.0
    },
    {
      "descrizione": "Tostapane",
      "prezzo": 20.0
    }
  ]
}
```