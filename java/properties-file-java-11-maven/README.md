# Utilizzo di properties file in Java

[Codice GitHub]()

## Descrizione

Questo tutorial mostra come creare un’applicazione Java 11 per recuperare le configurazioni applicative da file di properties.

## Prerequisiti

- Java 11
- Maven

## Aggiungere le dipendenze necessarie

Per la lettura delle properties si è scelto di sfruttare un'apposita libreria di Apache.

Per poterla utilizzare bisogna aggiungere nel file **pom.xml** le dipendenze **commons-configuration2** e **commons-beanutils**.

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-configuration2</artifactId>
    <version>2.8.0</version>
</dependency>

<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.3</version>
</dependency>
```

## Compilare il progetto indicando il JDK corretto

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

## Creare un eseguibile jar

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

## Creare una classe Main

Per l'utilizzo dell'applicazione bisogna creare una classe contenente un metodo **main**.

```java
package example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) throws ConfigurationException {
        String configFilePath = System.getProperty("config.dir") + System.getProperty("file.separator") + "application.properties";

        log.info("Loading properties from {}", configFilePath);

        Configurations configs = new Configurations();
        Configuration config = configs.properties(new File(configFilePath));

        log.info("Value for key1: {}", config.getString("key1"));
        log.info("Value for key2: {}", config.getString("key2"));
    }
}
```

## Compilare l'applicazione

Per compilare l'applicazione bisogna spostarsi nella cartella contenente il file **pom.xml** ed eseguire il seguente comando.

```bash
mvn clean package
```

## Eseguire l'applicazione

Per eseguire l'applicazione bisogna spostarsi nella cartella **target** ed eseguire il seguente comando.

```bash
java -Dconfig.dir=/path/to/config/dir -jar mia-applicazione-jar-with-dependencies.jar
```