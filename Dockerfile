# 1. On part d'un système Linux ultra léger avec Java 17 déjà installé
FROM eclipse-temurin:21-jdk-alpine

# 2. On crée un dossier de travail à l'intérieur du conteneur
WORKDIR /app

# 3. On copie notre fameux "Fat JAR" depuis notre PC vers l'intérieur du conteneur
COPY target/cookie-monsters-0.0.1-SNAPSHOT.jar server.jar

# 4. On indique que notre serveur va communiquer sur le port 8080
EXPOSE 8081

# 5. La commande exacte qui sera tapée quand le conteneur démarrera
CMD ["java", "-jar", "server.jar", "--spring.profiles.active=server"]