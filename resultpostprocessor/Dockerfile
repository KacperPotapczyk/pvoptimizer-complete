FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/resultpostprocessor-0.0.1-SNAPSHOT.jar resultpostprocessor.jar
COPY src/main/resources/application-compose.yml application-compose.yml
COPY src/main/resources/logback.xml logback.xml
ENTRYPOINT java -Dspring.profiles.active=compose -jar resultpostprocessor.jar