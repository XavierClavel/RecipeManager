FROM openjdk:25-jdk-slim
WORKDIR /app
COPY build/libs/mail-service-all.jar /app/mail-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "mail-service.jar"]
