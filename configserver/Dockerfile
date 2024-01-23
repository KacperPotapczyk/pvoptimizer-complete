FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver.jar
COPY src/main/resources/application.yml application.yml
ENTRYPOINT java -jar configserver.jar