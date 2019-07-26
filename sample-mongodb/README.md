Instruções de Uso
=================

Para a versão (2.2 a 2.6), executar os seguintes comandos dentro do SHELL do MongoDB:

```sh
$ use samples
$ db.addUser('samples', 'samples')
$ exit
```

Para a versão (2.6 a 3.6), executar os seguintes comandos dentro do SHELL do MongoDB:

```sh
$ use samples
$ db.createUser({user: "samples", pwd : "samples", roles: ["readWrite"]})
$ exit
```