# Selecting openjdk-alphine as base image which uses debian:stretch-slim as base
FROM openjdk:jdk-alpine
# Adding the jar to the image
ADD target/transcoder-0.0.1-SNAPSHOT.jar transcoder-0.0.1-SNAPSHOT.jar
# By default exposing the port
EXPOSE 7070
COPY crypto-hallway-244715-3b3f4d3e01b9.json /transcoder/crypto-hallway-244715-3b3f4d3e01b9.json
FROM node:alpine
RUN apk add  --no-cache ffmpeg
# Executes when the docker image is run
ENTRYPOINT ["java","-jar","transcoder-0.0.1-SNAPSHOT.jar"]