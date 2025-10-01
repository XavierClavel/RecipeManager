# Stage 1: Build Shadow JAR
FROM gradle:latest AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle clean shadowJar -x test --no-daemon

# Stage 2: Runtime
FROM amazoncorretto:22
WORKDIR /app
COPY --from=build /home/gradle/src/backend/build/libs/*-all.jar cooknco.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cooknco.jar"]
