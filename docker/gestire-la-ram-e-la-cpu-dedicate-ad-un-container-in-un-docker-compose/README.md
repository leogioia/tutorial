# Gestire la RAM e la CPU dedicate ad un container in un docker-compose

Alcune volte quando viene creato un nuovo container si incorre in un alto consumo di RAM e/o CPU del host su cui viene eseguito.

Questa problematica è facilmente risolvibile con l'utilizzo di determinati parametri di configurazione da esplicitare nel **docker-compose**.

### Prerequisiti

- Docker
- Docker-compose

### Creazione del file docker-compose.yml

Per creare un nuovo container tramite **docker-compose** bisogna creare il file **docker-compose.yml**.

Per gestire la RAM e la CPU bisogna aggiungere i seguenti parametri:
- **deploy.resources.limits.cpus**: CPU (espressa in Cores) massima che il container potrà utilizzare
- **deploy.resources.limits.memory**: RAM (espressa in Bytes) massima che il container potrà utilizzare
- **deploy.resources.reservations.cpus**: CPU (espressa in Cores) che la piattaforma dovrà garantire al container
- **deploy.resources.reservations.memory**: RAM (espressa in Bytes) che la piattaforma dovrà garantire al container

Nell'esempio viene creato un container **nginx** a cui:
- Vengono garantite **0.25 Cores** e **128 MB di RAM** 
- Viene limitato il consumo di risorse a **0.50 Cores** e **512 MB di RAM** 

```yaml
version: '3'

services:
  nginx:
    image: nginx
    container_name: nginx
    restart: always
    ports:
      - '80:80'
    deploy:
      resources:
        limits:
          cpus: "0.50"
          memory: "512M"
        reservations:
          cpus: "0.25"
          memory: "128M"
```