FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/sms-0.0.1-SNAPSHOT.jar sms.jar

EXPOSE 8080

CMD ["java", "-jar", "sms.jar"]