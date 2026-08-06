# We start with an ultra-lightweight Linux system that already has Java 17 installed
FROM eclipse-temurin:21-jdk-alpine

# We create a working directory inside the container
WORKDIR /app

# We copy the application JAR from the local machine into the container
COPY app.jar server.jar
# We specify that our server will communicate on port 8080
EXPOSE 8080

# The exact command that will be entered when the container starts
CMD ["java", "-jar", "server.jar", "--spring.profiles.active=server"]