FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/cookie-monsters-0.0.1-SNAPSHOT.jar server.jar

EXPOSE 8081

CMD ["java", "-jar", "server.jar", "--spring.profiles.active=server"]