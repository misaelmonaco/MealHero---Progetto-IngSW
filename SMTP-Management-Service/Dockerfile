FROM openjdk:17-jdk

COPY target/SMTP-Management-Service-0.0.1-SNAPSHOT.jar /app/SMTP-Management-Service-0.0.1-SNAPSHOT.jar

WORKDIR /app
EXPOSE 8088

ENTRYPOINT ["java", "-.jar", "SMTP-Management-Service-0.0.1-SNAPSHOT.jar"]