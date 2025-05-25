# 1. Use a base image with Java 21 installed
FROM openjdk:21-jdk-slim

# 2. Set working directory in the container
WORKDIR /app

# 3. Copy the built JAR into the container
COPY build/libs/spring-platimee-0.0.1-SNAPSHOT.jar app.jar

# 4. Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
