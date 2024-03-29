FROM openjdk:17
COPY build/libs/travel-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["sh", "-c", "java -jar /app.jar"]

