FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/calculationpreprocesor-0.0.1-SNAPSHOT.jar calculationpreprocesor.jar
COPY src/main/resources/application-compose.yml application-compose.yml
COPY src/main/resources/logback.xml logback.xml
ENTRYPOINT java -Dspring.profiles.active=compose -jar calculationpreprocesor.jar