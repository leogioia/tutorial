# Forzare l'utilizzo di uno specifico JDK con Maven

Questo tutorial mostra come creare un’applicazione Java 11 utilizzando maven.

[Codice GitHub](https://github.com/leogioia/tutorial/tree/master/java/forzare-utilizzo-di-uno-specifico-JDK-con-maven)

### Prerequisiti

- Java 11
- Maven

### Forzare l'utilizzo del JDK corretto

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
                        <message>JDK to use 11</message>
                        <version>[11,12)</version>
                    </requireJavaVersion>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Compilare il progetto indicando il JDK corretto

Per compilare il progetto utilizzando la corretta versione di Java bisogna aggiungere al file **pom.xml** il plugin maven **maven-compiler-plugin**.

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>11</source>
        <target>11</target>
    </configuration>
</plugin>
```

### Creare un eseguibile jar

Per creare un eseguibile jar, contenente anche le eventuali dipendenze utilizzare, bisogna aggiungere al file **pom.xml** il plugin maven **maven-assembly-plugin**.

All'interno del plugin bisogna indicare la classe contenente il main da utilizzare per l'avvio dell'applicazione.

Al termine della fase di **package** verrà creato un pacchetto jar all'interno della cartella target con suffisso **-jar-with-dependencies** che potrà essere utilizzato per l'avvio dell'applicazione.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.5.0</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>
                            example.Main
                        </mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Creare una classe Main

Per l'utilizzo dell'applicazione bisogna creare una classe contenente un metodo **main**.

```java
package example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        String params = String.join(",", args);

        log.info("Starting with params [{}]", params);
    }
}
```

### Compilare l'applicazione

Per compilare l'applicazione bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando.

```bash
mvn clean package
```

### Eseguire l'applicazione

Per eseguire l'applicazione bisogna spostarsi nella cartella **target** ed eseguire il seguente comando.

```bash
java -jar mia-applicazione-jar-with-dependencies.jar
```