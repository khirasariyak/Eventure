FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/eventure-*.jar app.jar
EXPOSE 8080
CMD ["sh", "-c", "echo 'Waiting 30s for Elasticsearch...'; sleep 30; java -jar app.jar"]
