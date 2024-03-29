# Build and select the jar
FROM openjdk:17 AS builder
ARG JAR_FILE=./build/libs/*-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Set up the final image
FROM openjdk:17
COPY --from=builder /app.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
