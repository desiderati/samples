# Abrir o console do Artemis

```shell
docker-compose exec -it artemis-server /var/lib/artemis-instance/bin/artemis shell --user artemis --password artemis
```

# Criar a fila

```shell
queue create --name=track-queue --address=track-queue --anycast --durable --preserve-on-no-consumers --auto-create-address
```
