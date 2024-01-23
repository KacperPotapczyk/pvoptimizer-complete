FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/apigateway-0.0.1-SNAPSHOT.jar apigateway.jar
COPY src/main/resources/application-compose.yml application-compose.yml
ENTRYPOINT java -Dspring.profiles.active=compose -jar apigateway.jar