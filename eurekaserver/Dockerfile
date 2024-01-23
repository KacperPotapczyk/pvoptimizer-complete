FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver.jar
COPY src/main/resources/application-compose.yml application-compose.yml
ENTRYPOINT java -Dspring.profiles.active=compose -jar eurekaserver.jar