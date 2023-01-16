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

# Run application

When runs the application, the `SetupDataLoader` will execute and the following data will be created (if they do not exist):

* Privileges: MANAGE_USERS, MANAGE_ROLES, MANAGE_PRIVILEGES, SHOW_LEASES, CREATE_LEASE, ACCEPT_LEASE, ADD_DETAILS_TO_LEASE
* Roles: ADMIN, LEASER, TENANT
* An admin user with `username: admin, password: testadmin123`

# User Manual

The user manual can be found at http://localhost:8080/swagger-ui/index.html# when the application is running. We used swagger for the documentation of the API.

![Screenshot 2023-01-16 at 10 18 49 AM](https://user-images.githubusercontent.com/122728171/212629984-a749a60b-ecd1-453a-acac-5a1b7ff12aab.png)


Links:
* [install docker](https://tinyurl.com/2m3bhahn)
* [Setting up swagger](https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api)
