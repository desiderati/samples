# Known Issues

## 1) **Error initializing the database: data directory not accessible.**

> postgres | initdb: could not change permissions of directory "/var/lib/postgresql/data": Operation not permitted

### Reason: The mapped volume `./data:/var/lib/postgresql/data` was probably created with incorrect permissions.

```yml
volumes:
  - ./data:/var/lib/postgresql/data
```

### Resolution: Run the file `init.sh`.

```bash
$ ./init.sh
```
