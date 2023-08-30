FROM openjdk:8
LABEL maintainer="laboratory6889@gmail.com"
ARG JAR_FILE=build/libs/nyangService-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT [java","-jar","/myboot.jar"]]