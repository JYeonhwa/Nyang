FROM openjdk:8
LABEL maintainer="laboratory6889@gmail.com"
ARG JAR_FILE=build/libs/nyang-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} docker-springboot.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]