# Gestire properties dedicate ai test in Spring Boot

Questo tutorial mostra come utilizzare un file di properties dedicate ai test in un applicativo Spring Boot.

Per l'esempio seguente viene implementata una classe contenente dei metodi dedicati alla lettura delle properties.

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/gestire-properties-dedicate-ai-test-in-spring-boot)

### Prerequisiti

- Java 17
- Maven
- Spring Boot 2.7.11

### Aggiungere Spring Boot nella sezione di dependency management al pom.xml

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

### Aggiungere le dipendenze necessarie al pom.xml

Per l'utilizzo dei Service di Spring bisogna aggiungere nel file **pom.xml** la dipendenza **spring-boot-starter-web**.

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Creare una classe Main

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

### Creare un @Service per la lettura di properties diverse da semplici String

In questo esempio vengono implementati i metodi:

1. **getInt**: per il parsing di una _String_ ad un _Integer_
2. **getArrayString**: per il parsing di una _String_ ad un array di _String_
3. **getMapStringInt**: per il parsing di una _String_ ad una _Map<String, Integer>_

```java
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PropertyService {

  public int getInt(String p) {
    String prop = Optional.ofNullable(p).orElse("").trim();
    return Integer.parseInt(prop);
  }

  public String[] getArrayString(String p) {
    String prop = Optional.ofNullable(p).orElse("");
    return prop.split(",");
  }

  public Map<String, Integer> getMapStringInt(String p) {
    String prop = Optional.ofNullable(p).orElse("");

    Map<String, Integer> result = new HashMap<>();

    Arrays.stream(prop.split(","))
            .forEach(pr -> result.put(
                    pr.substring(0, pr.indexOf(":")),
                    Integer.parseInt(pr.substring(pr.indexOf(":") + 1))
            ));

    return result;
  }
}
```

### Creare files di properties

Spring Boot supporta la lettura di un file di properties di default chiamato **application.properties** e situato nella
directory **src/main/resources**.

In questo esempio viene creato un secondo file di properties chiamato **application-unit.properties** e situato nella
directory **src/test/resources** per sfruttare le funzionalità offerte dai profili Spring in fase di test.

Il contenuto dei file è mostrato di seguito.

**application.properties**

```properties
mia.property.string=Stringa in application.properties main
mia.property.int=10
mia.property.array.string=Stringa1,Stringa2,Stringa3
mia.property.mappa.string.int=Stringa1:1,Stringa2:2,Stringa3:3
```

**application-unit.properties**

```properties
mia.property.string=Stringa in application-unit.properties
mia.property.int=1010
mia.property.array.string=Stringa11,Stringa22,Stringa33
mia.property.mappa.string.int=Stringa1:11,Stringa2:22,Stringa3:33
```

### Creare una classe di test per la lettura delle properties di test

In questo esempio vengono utilizzate le annotation:

1. **@SpringBootTest**: per lo start del contesto Spring Boot in fase di test
2. **@ActiveProfiles("unit")**: per indicare il profilo da utilizzare, nell'esempio viene utilizzato il profilo **unit** che andrà a caricare le properties dal file _application-**unit**.properties_

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@Slf4j
@SpringBootTest
@ActiveProfiles("unit")
public class PropertyServiceUnitTest {

    @Value("${mia.property.string}")
    String miaPropertyString;

    @Value("#{propertyService.getInt('${mia.property.int}')}")
    Integer miaPropertyInt;

    @Value("#{propertyService.getArrayString('${mia.property.array.string}')}")
    String[] miaPropertyArrayString;

    @Value("#{propertyService.getMapStringInt('${mia.property.mappa.string.int}')}")
    Map miaPropertyMappaStringInt;

    @Test
    public void testProperties(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getDisplayName());

        log.info("miaPropertyString: {}", miaPropertyString);
        log.info("miaPropertyInt: {}", miaPropertyInt);
        log.info("miaPropertyArrayString: {}", miaPropertyArrayString);
        log.info("miaPropertyMappaStringInt: {}", miaPropertyMappaStringInt);
    }
}
```

### Eseguire i test dell'applicazione

Per eseguire i test dell'applicazione bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente
comando:

```bash
mvn clean test
```

Si può notare come l'output mostra nei log il seguente contenuto (ovvero quello del file application-unit.properties):

```bash
logMessage=Start test PropertyServiceUnitTest.testProperties(TestInfo)
logMessage=miaPropertyString: Stringa in application-unit.properties
logMessage=miaPropertyInt: 1010
logMessage=miaPropertyArrayString: Stringa11
logMessage=miaPropertyMappaStringInt: {Stringa1=11, Stringa2=22, Stringa3=33}
```