FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY ./*.jar rabbit-communication-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "rabbit-communication-1.0-SNAPSHOT.jar"]