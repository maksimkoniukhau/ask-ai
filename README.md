# Running with local Ollama

To run only the Qdrant container and use Ollama installed on your host machine:

1. Start only Qdrant container:
```
docker compose up -d qdrant
```

2. Run Spring Boot app with default profile:
```
./gradlew bootRun
```

# Running with Ollama in docker

To run both Qdrant and Ollama in containers:

1. Start all services:
```
docker compose up -d
```

2. Run Spring Boot app with 'docker' profile
```
./gradlew bootRun -Dspring.profiles.active=docker
```
