# Selecting openjdk-alphine as base image which uses debian:stretch-slim as base
FROM openjdk:jdk-alpine
# Adding the jar to the image
ADD target/transcoder-0.0.1-SNAPSHOT.jar transcoder-0.0.1-SNAPSHOT.jar
# By default exposing the port
EXPOSE 9090
# Executes when the docker image is run
ENTRYPOINT ["java","-jar","transcoder-0.0.1-SNAPSHOT.jar"]