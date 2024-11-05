FROM gradle:8.10.2-jdk17-alpine AS build
WORKDIR /source
ADD .. .
RUN gradle clean build --no-daemon && rm -rf /source/build/libs/*-plain.jar && mv /source/build/libs/*.jar /app.jar && rm -rf /source

FROM openjdk:17.0.2-slim AS service
LABEL authors="akio"
WORKDIR /app

COPY --from=build /app.jar .

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
