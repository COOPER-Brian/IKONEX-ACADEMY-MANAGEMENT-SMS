# Stage 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Compile and package the JAR file, skipping unit tests for speed
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the compiled JAR file from the builder stage
COPY --from=builder /app/target/sms-0.0.1-SNAPSHOT.jar sms.jar

EXPOSE 8080

CMD ["java", "-jar", "sms.jar"]