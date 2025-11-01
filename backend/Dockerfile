FROM openjdk:25-jdk-slim
WORKDIR /app
COPY build/libs/backend-all.jar /app/backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "backend.jar"]
