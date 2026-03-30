# AI-Powered Code and Documentation Search

This demo project lets you search through source code and documentation files using AI. 
It reads files from a directory, converts them into vector embeddings, stores them in Qdrant for fast similarity search, 
and answers your questions based on the indexed content.

### Customize Docker data paths (optional)

If you want to store Docker data in custom locations, create a `.env` file in the project root and add env variables (see compose.yaml):

```
echo -e "DOCKER_OLLAMA_DATA=/home/docker/ollama\nDOCKER_QDRANT_DATA=/home/docker/qdrant" > .env
```

### Option 1: Using local Ollama

Use this if you already have Ollama installed and running on your machine.

1. Start only Qdrant:
```
docker compose up -d qdrant
```

2. Run Spring Boot app with default profile:
```
./gradlew bootRun
```

### Option 2: Using Ollama in Docker

Use this if you want everything running in Docker.

1. Start all services:
```
docker compose up -d
```

2. Download AI models (choose any model you prefer. Remember to update model names in `application.yaml`):
```
docker exec -it ollama ollama pull nomic-embed-text:latest
docker exec -it ollama ollama pull mistral:7b
```

3. Run Spring Boot app with 'docker' profile
```
./gradlew bootRun -Dspring.profiles.active=docker
```

### Using the API

Open your browser and go to:
```
http://localhost:8080/swagger-ui/index.html#/
```

From there you can:

1. Call `/embed-content` to populate the vector store (do this once to index your files).
2. Call `/ask` to ask questions and get AI-powered answers based on your indexed content.
