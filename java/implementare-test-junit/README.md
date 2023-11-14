# Implementare test JUnit

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/implementare-test-junit)

## Descrizione

Questo tutorial mostra come creare un’applicazione Java 11 utilizzando maven.

## Prerequisiti

- Java 17
- JUnit 5
- Maven

## Aggiungere le dipendenze necessarie al pom.xml

Per l'esecuzione dei test bisogna aggiungere nel file **pom.xml** la dipendenza **junit-jupiter**.

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.2</version>
    <scope>test</scope>
</dependency>
```

## Forzare l'utilizzo del JDK corretto

Per controllare l'utilizzo della versione di Java corretta bisogna aggiungere al file **pom.xml** il plugin maven **maven-enforcer-plugin**.
Nel caso in cui il JDK utilizzato non è quello specificato nel plugin, maven interromperà la build.

```xml
<plugin>
    <artifactId>maven-enforcer-plugin</artifactId>
    <version>1.4.1</version>
    <executions>
        <execution>
            <id>enforce-java</id>
            <goals>
                <goal>enforce</goal>
            </goals>
            <configuration>
                <rules>
                    <requireJavaVersion>
                        <message>JDK to use 17</message>
                        <version>[17,18)</version>
                    </requireJavaVersion>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## Compilare il progetto indicando il JDK corretto

Per compilare il progetto utilizzando la corretta versione di Java bisogna aggiungere al file **pom.xml** il plugin maven **maven-compiler-plugin**.

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
    </configuration>
</plugin>
```

## Creare una classe contenente la logica di business

Per poter creare un test, bisogna prima creare la classe su cui eseguire il test sotto la directory **src/main/java**.

In questo esempio verrà utilizzata una semplice classe Calcolatrice che utilizzerà due metodi per eseguire la somma e la differenza di due numeri interi.

```java
public class Calcolatrice {

    public int somma(int num1, int num2) {
        return num1 + num2;
    }

    public int differenza(int num1, int num2) {
        return num1 - num2;
    }
}
```

## Creare una classe di test

Per l'esecuzione dei test bisogna creare una classe sotto la directory **src/test/java**.

In questo esempio verrà implementata una classe contenente due test, uno per testare la somma e uno la differenza.

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@Slf4j
public class CalcolatriceTest {

    @Test
    public void testSomma(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getTestMethod().get().getName());

        Calcolatrice calcolatrice = new Calcolatrice();

        int num1 = 27;
        int num2 = 53;

        int expected = 80;

        Assertions.assertEquals(expected, calcolatrice.somma(num1, num2));
    }

    @Test
    public void testDifferenza(TestInfo testInfo) {
        log.info("Start test {}.{}", getClass().getSimpleName(), testInfo.getTestMethod().get().getName());

        Calcolatrice calcolatrice = new Calcolatrice();

        int num1 = 100;
        int num2 = 20;

        int expected = 80;

        Assertions.assertEquals(expected, calcolatrice.differenza(num1, num2));
    }
}
```

## Eseguire i test

Per eseguire i test bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando.

```bash
mvn clean test
```

Al termine dell'esecuzione è possibile recuperare l'esito dei test nell'output della console:

```bash
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running it.leogioia.test.CalcolatriceTest
timestamp=0000-00-00 00:00:00.000|logLevel=INFO|thread=main|logMessage=Start test CalcolatriceTest.testSomma
timestamp=0000-00-00 00:00:00.000|logLevel=INFO|thread=main|logMessage=Start test CalcolatriceTest.testDifferenza
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.000 s - in CalcolatriceTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.000 s
[INFO] Finished at: 0000-00-00T00:00:00+00:00
[INFO] ------------------------------------------------------------------------
```