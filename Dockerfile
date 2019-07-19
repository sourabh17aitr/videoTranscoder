# Selecting openjdk-alphine as base image which uses debian:stretch-slim as base
FROM openjdk:jdk-alpine
# Adding the jar to the image
ADD target/transcoder-0.0.1-SNAPSHOT.jar transcoder-0.0.1-SNAPSHOT.jar
# By default exposing the port
EXPOSE 9090
COPY crypto-hallway-244715-3b3f4d3e01b9.json /transcoder/crypto-hallway-244715-3b3f4d3e01b9.json
# Downloading ffmpeg and moving the file to /usr/local/bin/
RUN wget https://johnvansickle.com/ffmpeg/builds/ffmpeg-git-64bit-static.tar.xz
RUN tar xvf ffmpeg-git-*.tar.xz
# Moving ffmpeg to bin folder
USER root
RUN mv ffmpeg-git-*/* /usr/bin/
# Setting env variable for ffmpeg
ENV ffmpeg_home /usr/bin/ffmpeg
# Executes when the docker image is run
ENTRYPOINT ["java","-jar","transcoder-0.0.1-SNAPSHOT.jar"]