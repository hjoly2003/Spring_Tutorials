# [N]:docker - The Dockerfile allows you to execute your microservice. With the multistage build, you’ll copy only what’s essential to the application.
# [TODO] When is it required? Can we get without it by using the docker-maven-plugin?

### stage 1

# Start with a base image containing Java runtime
FROM openjdk:11-jre as builder
WORKDIR application
# [N] Defines the application's ARTIFACT_NAME variable set by dockerfile-maven-plugin (line 155 in pom.xml)
ARG ARTIFACT_NAME
# Copies the JAR file to the filesystem of the image named application.jar.
COPY ${ARTIFACT_NAME}.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract


### stage 2

# Download dockerize and cache that layer
# [N] The DOCKERIZE_VERSION variable is set by dockerfile-maven-plugin (line 157 in pom.xml)
ARG DOCKERIZE_VERSION
RUN wget -O dockerize.tar.gz https://github.com/jwilder/dockerize/releases/download/${DOCKERIZE_VERSION}/dockerize-alpine-linux-amd64-${DOCKERIZE_VERSION}.tar.gz
RUN tar xzf dockerize.tar.gz
RUN chmod +x dockerize


### stage 3

# Note: multistage allows us to discard anything that isn’t essential to the execution of the application. This new image contains the different layers of a Spring Boot app instead of the complete JAR file.

# wget is not installed on adoptopenjdk:11-jre-hotspot
FROM adoptopenjdk:11-jre-hotspot

WORKDIR application

# Dockerize
COPY --from=builder application/dockerize ./

ARG EXPOSED_PORT
EXPOSE ${EXPOSED_PORT}

ENV SPRING_PROFILES_ACTIVE docker

# Copies the different layers from the first image named build
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# executes the application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
