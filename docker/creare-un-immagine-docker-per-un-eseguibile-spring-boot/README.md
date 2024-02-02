# Creare un’immagine docker per un eseguibile Spring Boot

Dopo lo sviluppo di un progetto Spring Boot viene creato un file jar eseguibile che per essere utilizzato ha bisogno di un runtime in cui eseguire.

Una delle scelte ad oggi comune è quella di utilizzare un'apposita immagine docker custom come runtime.

### Prerequisiti

- Docker

### Creazione del file Dockerfile

Per creare una nuova immagine docker è necessario creare un file chiamato **Dockerfile** in cui specificare le informazioni necessarie all'esecuzione del jar.

In questo esempio vengono utilizzati:
1. **FROM eclipse-temurin:17**: per indicare che l'immagine di partenza è **eclipse-temurin:17** (un'immagine contente già una JVM 17)
2. **RUN mkdir /opt/app**: per la creazione di una directory in cui copiare il jar da eseguire
3. **COPY path/to/jar /opt/app/app.jar**: per copiare il jar da eseguire nella cartella creata
4. **CMD ["java", "-jar", "/opt/app/app.jar"]**: per specificare il comando e i parametri per l'eseguzione del jar

```dockerfile
FROM eclipse-temurin:17
RUN mkdir /opt/app
COPY path/to/jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
```

### Build dell'immagine

Per eseguire la build dell'immagine è sufficiente spostarsi nella directory contenente il *Dockerfile* e lanciare il comando:

```bash
docker build -t image-name:1.0.0 .
```

Se si desidera utilizzare un Dockerfile diverso da quello presente nella cartella dove si esegue la build bisogna aggiungere il parametro **-f path/to/Dockerfile** al comando sopra.

### Run del container

Per eseguire un container dell'immagine appena creata bisogna lanciare il comando:

```bash
docker run image-name:1.0.0
```