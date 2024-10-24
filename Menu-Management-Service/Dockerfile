FROM openjdk:17-jdk

# Variabili d'ambiente
ENV API_USERNAME=root
ENV API_PASSWORD=password

COPY target/Menu-Management-Service-0.0.1-SNAPSHOT.jar /app/Menu-Management-Service-0.0.1-SNAPSHOT.jar

WORKDIR /app
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "Menu-Management-Service-0.0.1-SNAPSHOT.jar"]