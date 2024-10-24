FROM openjdk:17-jdk

# Variabili d'ambiente
ENV API_USERNAME=root
ENV API_PASSWORD=password

COPY target/Reservation-Service-0.0.1-SNAPSHOT.jar /app/Reservation-Service-0.0.1-SNAPSHOT.jar

WORKDIR /app
EXPOSE 8085

ENTRYPOINT ["java", "-.jar", "Reservation-Service-0.0.1-SNAPSHOT.jar"]