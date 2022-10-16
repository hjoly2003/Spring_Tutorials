# Uses Java 11 from AdoptOpenJDK (https://adoptopenjdk.net/)
# FROM adoptopenjdk:11-jre-hotspot
FROM arm64v8/openjdk:17.0.1-jdk-oraclelinux7

# Add Maintainer Info
LABEL maintainer="baeldung.com"

# Copies the application JAR file to application.jar.
COPY ./target/*.jar application.jar

# Runs that JAR file to start our Spring Boot application.
ENTRYPOINT ["java", "-jar", "application.jar"]
