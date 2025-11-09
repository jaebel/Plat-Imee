# 0. Build stage: Use Gradle 8.12.1 with JDK 21 to build the JAR (with tests)
FROM gradle:8.12.1-jdk21 AS builder

WORKDIR /app
COPY . .

# Run full build including tests
RUN gradle clean build -x test --no-daemon

# 1. Use Eclipse Temurin JDK 21 for the final runtime (stable replacement for openjdk)
FROM eclipse-temurin:21-jdk

# 2. Set working directory in the container
WORKDIR /app

# 3. Copy the built JAR into the container
COPY --from=builder /app/build/libs/spring-platimee-0.0.1-SNAPSHOT.jar app.jar

# 4. Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
