# Implementare test JUnit per api REST in Spring Boot

Questo tutorial mostra come implementare dei test JUnit per richiamare un'api REST esposta utilizzando Spring Boot.

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/implementare-test-junit-api-rest-spring-boot)

## Prerequisiti

- Java 17
- Maven
- Spring Boot 2.7.11

## Aggiungere le dipendenze Spring Boot necessarie a lanciare i test al pom.xml

Per l'implementazione dei test per un applicazione Spring Boot bisogna aggiungere nel file **pom.xml** la dipendenza **spring-boot-starter-test**.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <version>2.5.0</version>
</dependency>
```

## Aggiungere la dipendenza JUnit al pom.xml

Per l'implementazione dei test bisogna aggiungere nel file **pom.xml** la dipendenza **junit-jupiter**.

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

## Aggiungere l'annotazione @SpringBootTest alla classe di test

Per l'esecuzione di un test tramite Spring Boot bisogna aggiungere l'annotation @SpringBootTest alla classe di test.

L'applicazione utilizzata espone delle api REST, per evitare possibili collisioni sulle porte esposte sulla macchina locale viene istruito Spring Boot ad utilizzare una porta random tramite l'annotation **@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)**.

Per il recupero della porta scelta da Spring Boot bisogna annotare un intero con l'annotation **@LocalServerPort**.

```java
package example;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnagraficaApiTest {

    @LocalServerPort
    private int port;
    
    ...
}
```

## Creare un @Test

Per creare un semplice test JUnit bisogna annotare il metodo con l'annotation **@Test**.

Nell'esempio il test esegue una chiamata ad un'api REST e verifica lo stato HTTP della risposta e il contenuto del body.

```java
@Test
public void testGetAnagrafiche(TestInfo testInfo) throws Exception {
        log.info("Start test {}", testInfo.getTestMethod().get().getName());
        String requestUrl = "http://localhost:" + port + "/api/v1/anagrafiche";

        OkHttpClient client = new OkHttpClient()
        .newBuilder()
        .build();

        Request request = new Request.Builder()
        .url(requestUrl)
        .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        Assertions.assertEquals(200, response.code());
        Assertions.assertTrue(JsonUtil.fromJsonToObjectList(Anagrafica.class, response.body().string()).size() > 0);
}
```

## Creare un @ParameterizedTest

Per creare un test JUnit parametrizzato bisogna annotare il metodo con le annotation:
1. **@ParameterizedTest**
2. **@CsvSource** aggiungendo i valori con cui invocare il metodo di test

Nell'esempio il test esegue 2 chiamate a 2 api REST, una per censire un'anagrafica e una per recuperare l'anagrafica appena censita.

```java
@ParameterizedTest
@CsvSource({
        "2,Giuseppe,Bianchi,Via Garibaldi",
        "3,Marco,Verdi,Via Marconi"
})
public void testPostAndGetAnagrafica(String id, String nome, String cognome, String indirizzo, TestInfo testInfo) throws Exception {
        log.info("Start test {} with param: id={}, nome={}, cognome={}, indirizzo={}",
        testInfo.getTestMethod().get().getName(),
        id, nome, cognome, indirizzo);

        OkHttpClient client = new OkHttpClient()
        .newBuilder()
        .build();

        String requestUrl = "http://localhost:" + port + "/api/v1/anagrafiche";

        String expected = FileUtil.readResourceFile("expected/anagrafica.json")
        .replace("${id}", id)
        .replace("${nome}", nome)
        .replace("${cognome}", cognome)
        .replace("${indirizzo}", indirizzo);

        RequestBody requestBody = RequestBody.create(
        MediaType.parse("application/json"), expected);

        Request request = new Request.Builder()
        .url(requestUrl)
        .post(requestBody)
        .build();

        Call call = client.newCall(request);

        Response response = call.execute();

        Assertions.assertEquals(200, response.code());

        request = new Request.Builder()
        .url(requestUrl + "/" + id)
        .build();

        call = client.newCall(request);

        response = call.execute();

        Assertions.assertEquals(200, response.code());
        JsonUtil.compareObject(expected, response.body().string());
}
```

## Esecuzione automatica dei test

Per eseguire i test in modo automatico bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando:

```bash
mvn clean test
```