# Alternative per la gestione delle properties applicative in test Spring Boot

Lo sviluppo di un'applicazione di buon livello prevede l'utilizzo di configurazioni applicative che potrebbero dover essere sovrascritte durante la fase di test unitari con delle properties dedicate unicamente ai test.

Spring Boot mette a disposizione diverse modalità per la gestione delle properties dedicate ai test, tra cui:
1. **Utilizzo di profili applicativi dedicati ai test**
2. **Utilizzo di un file di properties dedicato ai test**

### Utilizzo di profili applicativi

Questa modalità prevede l'utilizzo di un profilo Spring Boot dedicato ai test per specificare quali sono le properties da utilizzare.
Ad esempio, supponendo di avere la seguente alberatura:

```bash
src
| - test
  | - resources
    | - application-unit.properties
```

Si può utilizzare l'annotation **@ActiveProfiles("unit")** per indicare che si vuole utilizzare il profilo **unit** e quindi il file **application-unit.properties**:

```java
@SpringBootTest
@ActiveProfiles("unit")
class MiaClasseTest {
    .
    .
    .
}
```

### Utilizzo di un file di properties dedicato ai test

Questa modalità permette di specificare direttamente il file di properties che si desidera utilizzare durante l'esecuzione dei test.
Ad esempio, supponendo di avere la seguente alberatura:

```bash
src
| - test
  | - resources
    | - unit
      | - application.properties
```

Si può utilizzare l'annotation **@TestPropertySource(locations = "classpath:unit/application.properties")** per indicare che si vuole utilizzare il file **application.properties** presente tra le risorse di test nella directory **unit**:

```java
@SpringBootTest
@TestPropertySource(locations = "classpath:unit/application.properties")
class MiaClasseTest {
    .
    .
    .
}
```