# customer-service

This is a REST service to manager customer information

See openapi.yaml for more information

## Running the code in dev mode

You can run your code in dev mode that enables live coding using:

```sh
./mvnw compile quarkus:dev
```

> ___NOTE:___  Quarkus now ships with a Dev UI, which is available in DEV mode only at http://localhost:8080/q/dev/.

> ___NOTE:___ DEV mode will check the JDBC driver you have configured in your project. In this service, we used jdbc-postgresql, so it will start a Postgresql database: **Make sure to have Docker or Podman running!**

## Packaging and running the service

The service can be packaged using:

```sh
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The service is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.