FROM openjdk:17.0.2-slim

WORKDIR /data
ADD build/libs .
RUN rm -rf *-plain.jar && mv *.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
