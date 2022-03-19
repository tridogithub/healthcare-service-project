FROM adoptopenjdk/openjdk11:latest
ARG JAR_FILE=target/healthcare-service-*.jar
COPY ${JAR_FILE} /opt/app/healthcare_service.jar
ENTRYPOINT ["java", "-jar", "/opt/app/healthcare_service.jar"]