# Spring REST API with OpenAPI Generator

This project is a simple showcase of a REST API architecture. The API is formally defined using
an [OpenAPI specification](src/main/resources/openapi-spec.yml), and the Java Spring Boot interface is generated with
the [`openapi-generator-maven-plugin`](https://openapi-generator.tech/docs/plugins/).

## Start runtime dependencies

This projects depends on a PostgreSQL database which can be started by executing the docker compose file provided by
this repo. Start the container by prompting:

``` bash
cd docker
docker compose up -d
```

## Fetch API

For interacting with the API either use the provided [`swagger-ui`](http://localhost:8080/swagger-ui/index.html) or use the
commands below.

### GET /car

Request to get all cars from the database.

``` bash
curl -u "admin:admin" http://localhost:8080/car
```

### GET /car/<car-id>

Request to get a specific car from the database by its id.

``` bash
export CAR_ID="<car-id>"

curl -u "admin:admin" http://localhost:8080/car/$CAR_ID
```

### POST /car

Request to create a car in the database.

``` bash
export CREATE_CAR='{"make": "mercedes", "model": "SL500", "mileage": 15000}'

curl -X POST -u "admin:admin" http://localhost:8080/car -H 'Content-Type: application/json' -d $CREATE_CAR
```

### PUT /car

Request to update a car in the database.

``` bash
export UPDATE_CAR='{"id": "<car-id>","make": "mercedes","model": "A250","mileage": 15000}'

curl -X PUT -u "admin:admin" http://localhost:8080/car -H 'Content-Type: application/json' -d $UPDATE_CAR
```

### DELETE /car/<car-id>

Request to delete a car in the database.

``` bash
export CAR_ID="<car-id>"

curl -X DELETE -u "admin:admin" http://localhost:8080/car/$CAR_ID
```