# Selecting openjdk-alphine as base image which uses debian:stretch-slim as base
FROM openjdk:jdk-alpine
# Adding the jar to the image
ADD target/transcoder-0.0.1-SNAPSHOT.jar transcoder-0.0.1-SNAPSHOT.jar
# By default exposing the port
EXPOSE 7070
COPY crypto-hallway-244715-3b3f4d3e01b9.json /transcoder/crypto-hallway-244715-3b3f4d3e01b9.json
RUN wget https://johnvansickle.com/ffmpeg/builds/ffmpeg-git-amd64-static.tar.xz
RUN tar xvf ffmpeg-git-*.tar.xz
# Moving ffmpeg to bin folder
RUN mv ffmpeg-git-*/* /usr/bin/
# Executes when the docker image is run
ENTRYPOINT ["java","-jar","transcoder-0.0.1-SNAPSHOT.jar"]