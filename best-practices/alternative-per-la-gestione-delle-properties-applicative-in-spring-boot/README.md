# Alternative per la gestione delle properties applicative in Spring Boot

Lo sviluppo di un'applicazione di buon livello prevede l'utilizzo di configurazioni che permettano di rilasciarla in diversi ambienti con facilità.

Spring Boot permette di gestire le configurazioni applicative tramite l'utilizzo di file di properties in diverse modalità, tra cui:
1. **Utilizzo di profili applicativi**
2. **Utilizzo di variabili d'ambiente**
3. **Utilizzo di un file di properties esterno al pacchetto jar**

Negli esempi che seguono si ipotizza di avere a disposizione 1 ambiente locale a disposizione dello sviluppatore e 3 ambienti di integrazione:
1. **local**
2. **development**
3. **staging**
4. **production**

### Utilizzo di profili applicativi

Questa modalità prevede l'utilizzo dei profili di Spring Boot, i quali permettono di specificare il file di properties da utilizzare tra quelli presenti nella directory _src/main/resources_.
Ad esempio, supponendo di avere la seguente alberatura:

```bash
src
| - main
  | - resources
    | - application-local.properties
    | - application-development.properties
    | - application-staging.properties
    | - application.properties
```

Si può utilizzare la property **spring.profiles.active** per specificare il profile da utilizzare, quindi per lanciare il profile **local** si può utilizzare il comando:

```bash
java -Dspring.profiles.active=local -jar mia-app.jar
```

**Pro**
1. Semplicità della gestione del progetto in quanto i files di properties sono in un unico punto vicino al codice

**Contro**
1. In caso di modifiche unicamente alle configurazioni si rende necessario ricompilare l'intero progetto per creare il nuovo jar contenente le nuove configurazioni
2. Il ciclo di vita delle configurazioni è legato al ciclo di vita del codice, quindi in caso di modifiche contemporanee a codice e configurazioni bisogna tener conto della complessità di gestione della creazione del pacchetto

### Utilizzo di variabili d'ambiente

Questa modalità prevede l'utilizzo di un unico file di properties _src/main/resources/application.properties_ che preveda la dichiarazione delle properties come segue:

```bash
PROPERTY_1=${PROPERTY_1}
PROPERTY_2=${PROPERTY_2}
PROPERTY_3=${PROPERTY_3}
PROPERTY_4=${PROPERTY_4}
```

Al momento del rilascio è necessario dichiarare nell'ambiente dove si desidera rilasciare le variabili d'ambiente PROPERTY_1, PROPERTY_2, PROPERTY_3 e PROPERTY_4 con i valori desiderati.

**Pro**
1. Semplicità della gestione del file di properties in quanto vi è un unico file da manutenere
2. Semplicità della gestione del progetto in quanto il file di properties è in un unico punto vicino al codice
3. In caso di modifiche unicamente alle configurazioni è sufficiente modificare le variabili d'ambiente e fare un restart dell'applicazione
4. Il ciclo di vita delle configurazioni è slegato dal ciclo di vita del codice, quindi in caso di modifiche contemporanee a codice e configurazione non vi sono impatti significativi

**Contro**
1. Se due applicativi hanno dichiarato la stessa chiave a livello di ambiente, la gestione delle variabili d'ambiente si complica (nel caso in cui si utilizzi un runtime Docker per ogni applicazione questo contro viene meno)
2. Bisogna dichiarare e tenere allineati 1 file all'interno del pacchetto jar (application.properties) e 1 file per ogni ambiente contenete le chiavi da utilizzare come variabile di ambiente

### Utilizzo di file di configurazione esterni al pacchetto jar

Questa modalità prevede l'utilizzo di file di configurazione esterni al pacchetto jar, quindi dichiarati in una directory esterna alla directory _src_.
Un esempio di alberatura può essere la seguente:

```bash
config
| - local/application.properties
| - development/application.properties
| - staging/application.properties
| - production/application.properties
src
| - main
  | - resources
.
.
.
```

All'interno del progetto è necessario aggiungere una classe di configuration che sia annotata con **@PropertySource(value = {"file:${service.config.dir}/application.properties"})** e dichiari un Bean **PropertySourcesPlaceholderConfigurer**.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = {"file:${service.config.dir}/application.properties"})
public class GeneralConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
```

Si può utilizzare la property **service.config.dir** per specificare il path da utilizzare per il recupero del file di properties, quindi per lanciare il profile **local** si può utilizzare il comando:

```bash
java -Dservice.config.dir=config/local -jar mia-app.jar
```

**Pro**
1. Non vi sono problemi in caso in cui 2 applicativi dichiarino le stesse chiavi in quanto si trovano su file distinti
2. In caso di modifiche unicamente alle configurazioni è sufficiente modificare la property passata come parametro di start dell'applicazione
3. Il ciclo di vita delle configurazioni è slegato dal ciclo di vita del codice, quindi in caso di modifiche contemporanee a codice e configurazione non vi sono impatti significativi

**Contro**
1. In caso di utilizzo di runtime Docker è necessario creare un'immagine che contenga anche il file di configurazioni