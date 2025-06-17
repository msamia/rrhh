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
endpoints.

For deletions, pass both the employee id and the `contratoId` as a query
parameter, e.g. `DELETE /api/saga/empleado-contrato/5?contratoId=10`.
Use `GET /api/saga/empleado-contrato/{sagaId}` to inspect a saga's state.

