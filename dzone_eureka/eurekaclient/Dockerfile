FROM openjdk:11-slim as build
EXPOSE 8080
ADD /target/eurekaclient-0.0.1-SNAPSHOT.jar eurekaclient-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","eurekaclient-0.0.1-SNAPSHOT.jar"]