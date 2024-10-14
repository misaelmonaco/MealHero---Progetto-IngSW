FROM openjdk:17-jdk

# Variabili d'ambiente
ENV API_USERNAME=root
ENV API_PASSWORD=password

COPY target/Payment-Management-Service-0.0.1-SNAPSHOT.jar /app/Payment-Management-Service-0.0.1-SNAPSHOT.jar

WORKDIR /app
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "Payment-Management-Service-0.0.1-SNAPSHOT.jar"]