FROM openjdk:11-jre-slim
COPY /target/app-with-docker-0.0.1-SNAPSHOT.jar /app/hw-32.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/hw-32.jar"]