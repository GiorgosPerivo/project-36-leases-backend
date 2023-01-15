### Run a postgres database

By downloading and installing postgres

https://www.postgresql.org/download/

Then create a database with the name "leases_db"

or By docker

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=leases_db --net=host -v pgdata14:/var/lib/postgresql/data  -d postgres:14
```

## remove db data
```bash
docker volume rm pgdata14
```

In application.properties set the password and the db name that you chose

```
spring.datasource.url=jdbc:postgresql://localhost:5432/leases_db
spring.datasource.username=postgres
spring.datasource.password=pass123
```

When runs the application, the `SetupDataLoader` will execute and the following data will be created (if they do not exist):

* Privileges: MANAGE_USERS, MANAGE_ROLES, MANAGE_PRIVILEGES, SHOW_LEASES, CREATE_LEASE, ACCEPT_LEASE, ADD_DETAILS_TO_LEASE
* Roles: ADMIN, LEASER, TENANT
* An admin user with `username: admin, password: testadmin123`

Links:
* [install docker](https://tinyurl.com/2m3bhahn)