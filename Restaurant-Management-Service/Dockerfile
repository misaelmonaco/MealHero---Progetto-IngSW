FROM openjdk:17-jdk

# Variabili d'ambiente
ENV API_USERNAME=root
ENV API_PASSWORD=password

COPY target/Restaurant-Management-Service-0.0.1-SNAPSHOT.jar /app/Restaurant-Management-Service-0.0.1-SNAPSHOT.jar

WORKDIR /app
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "Restaurant-Management-Service-0.0.1-SNAPSHOT.jar"]