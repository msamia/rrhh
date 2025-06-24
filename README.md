# RRHH Microservices

This repository contains several Spring Boot services that compose the human resources system.

## Prerequisites

- **JDK 17** must be installed and available in your `PATH`.

- **MariaDB** should be running locally for development/production profiles. Create databases named `contrato`, `empleado`, `entrenamiento`, `nomina`, `consultas` and `orquestador`.


- **MariaDB** should be running locally for development/production profiles. Create databases named `contrato`, `empleado`, `entrenamiento`, `nomina`, `consultas` and `orquestador`.


- **MariaDB** should be running locally for development/production profiles. Create databases named `contrato`, `empleado`, `entrenamiento`, `nomina`, `consultas` and `orquestador`.

- **MariaDB** should be running locally for development/production profiles.



- **Docker** and **Docker Compose** for optional infrastructure (Kafka and Zookeeper). A `compose.yaml` file is provided.

Tests use the in-memory **H2** database so no additional setup is required for them.

## Building and Running Services

Each service module contains its own Maven wrapper script. Navigate to the desired module and execute:

```bash
./mvnw spring-boot:run
```

Available modules include:

- `comunes`
- `servicio-contrato`
- `servicio-empleado`
- `servicio-entrenamiento`
- `servicio-nomina`
- `servicio-orquestador`
- `servicio-consultas`
- `API-gateway`
- `servidor-para-descubrimiento`

From the repository root you can build all modules at once:

```bash
./mvnw clean package
```

## Running Tests

Execute tests for all modules with:

```bash
./mvnw test
```

or inside a specific module:

```bash
cd servicio-contrato
./mvnw test
```

## Docker Compose

A minimal Docker Compose configuration is included to start Kafka and Zookeeper required by the services:

```bash
docker compose up -d
```

Ensure your local MariaDB instance is running with those databases created before starting the services. Tests will automatically use H2.


## Diagrams

PlantUML diagrams documenting the SAGA orchestration are available inside
`servicio-orquestador/docs/uml`. Additional sequence, activity, state, class and
component diagrams reside under `servicio-orquestador/docs/uml/modelos`.


Ensure your local MariaDB instance is running with those databases created before starting the services. Tests will automatically use H2.


Ensure your local MariaDB instance is running with those databases created before starting the services. Tests will automatically use H2.

Ensure your local MariaDB instance is running before starting the services. Tests will automatically use H2.





## Postman Tests

Sample Postman collection files are provided under `docs/postman`. Import
`rrhh-saga-tests.postman_collection.json` into Postman to try the SAGA
create, update, delete and status flows via the `/api/saga/empleado-contrato`
endpoints. The file `consultas-saga-tests.postman_collection.json` contains
additional requests that validate how `servicio-consultas` is updated after
each POST, PUT and DELETE operation. The response from the initial POST stores
the generated employee and contract identifiers in the collection variables
`{{empleadoId}}` and `{{contratoId}}` so that the following requests can reuse
them automatically.

For deletions, pass both the employee id and the `contratoId` as a query
parameter, e.g. `DELETE /api/saga/empleado-contrato/5?contratoId=10`.
Use `GET /api/saga/empleado-contrato/{sagaId}` to inspect a saga's state.

To check if the circuit breaker for employee creation opens, make several failing
requests (for instance by stopping `servicio-empleado`) and then send a `GET`

request to `http://localhost:8095/actuator/resilience4j/circuitbreakers/crearEmpleadoCB?includeState=true`.
`management.endpoints.web.exposure.include` list contains `resilience4j.circuitbreakers`
If the endpoint returns *405 Method Not Allowed*, verify that the
`management.endpoints.web.exposure.include` list contains `circuitbreakers`
and that the `management.endpoint.circuitbreakers.enabled` property is set to
`true` in
`servicio-orquestador/src/main/resources/application.properties` so the
actuator endpoint is available.


The `Estado Circuit Breaker crearEmpleadoCB` request in the Postman collection
expects the breaker state to be `OPEN`.

