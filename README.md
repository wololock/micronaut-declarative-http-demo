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
cd api/

./gradlew shadowJar

java -Dmicronaut.server.port=-1 -jar build/libs/api-0.1-all.jar
```

