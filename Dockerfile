# Stage 1: Cache Gradle dependencies
FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle.* gradle.properties settings.gradle.* /home/gradle/app/
COPY backend /home/gradle/app/backend
COPY common /home/gradle/app/common
WORKDIR /home/gradle/app
RUN gradle clean build -i --stacktrace -x test

# Stage 2: Build Application
FROM gradle:latest AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/app/
WORKDIR /usr/src/app
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle shadowJar --no-daemon -x test && find / -name "*.jar"

# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080:8080
RUN ls -l /home/gradle/app
RUN ls -l /home/gradle/app/backend
RUN pwd
RUN mkdir /app
COPY --from=build /home/gradle/app/build/libs/*.jar /app/cooknco.jar
ENTRYPOINT ["java","-jar","/app/cooknco.jar"]