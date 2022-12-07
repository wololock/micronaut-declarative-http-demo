# Micronaut Declarative HTTP Client Demo

Start docker containers:

```
docker-compose up -d
```

Create SQS queue using `awslocal` command-line tool:

```
awslocal sqs create-queue --queue-name create-user-request
```

Compile the `api` app and run it on a random port (requires Java 17):

```
cd users-api/

./gradlew shadowJar

java -Dmicronaut.server.port=-1 -jar build/libs/users-api-0.1-all.jar
```

## Testing application

* https://httpie.io/

```
http localhost:8080/client/users
```

```
http POST localhost:8080/client/users name="Foo" email="bar@example.com"
```

## Links mentioned in the presentation

* https://micronaut.io
* https://docs.micronaut.io/latest/guide/
* https://guides.micronaut.io/latest/index.html
* https://micronaut.io/launch