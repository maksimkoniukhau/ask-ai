# AI-Powered Code and Docs Search

This demo project reads source code and documentation files from a directory, 
creates vector embeddings, stores them in Qdrant for fast search, 
and lets users ask questions with AI-powered answers based on those files. 
It showcases how to build an AI-assisted knowledge retrieval system for codebases and documentation.

### Running with local Ollama

To run only the Qdrant container and use Ollama installed on your host machine:

1. Start only Qdrant container:
```
docker compose up -d qdrant
```

2. Run Spring Boot app with default profile:
```
./gradlew bootRun
```

### Running with Ollama in docker

To run both Qdrant and Ollama in containers:

1. Start all services:
```
docker compose up -d
```

2. Run Spring Boot app with 'docker' profile
```
./gradlew bootRun -Dspring.profiles.active=docker
```

### Using the API

After running the project, open your browser and navigate to:
```
http://localhost:8080/swagger-ui/index.html#/
```
Here you can:

1. Use the /embed-content endpoint to populate the vector store (this needs to be done only once to index your files).
2. Use the /ask endpoint to send questions and receive answers based on your indexed content.
