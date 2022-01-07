# [N]:docker - The Dockerfile allows you to execute your microservice. With the multistage build, you’ll copy only what’s essential to the application. 

#stage 1
#Start with a base image containing Java runtime
FROM openjdk:11-slim as build

# Add Maintainer Info
LABEL maintainer="Hugues Joly <Huguesjoly73@gmail.com>"

# [N] Defines the application's JAR_FILE variable set by dockerfile-maven-plugin (line 71 in pom.xml)
ARG JAR_FILE

# Copies the JAR file to the filesystem of the image named app.jar.
COPY ${JAR_FILE} app.jar

# Unpackage jar file into the filesystem of the build image. It unpack the jar to expose the different layers that a Spring Boot application contains.
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)


#stage 2
# Note: multistage allows us to discard anything that isn’t essential to the execution of the application. This new image contains the different layers of a Spring Boot app instead of the complete JAR file.

#Same Java runtime
FROM openjdk:11-slim

#Add volume pointing to /tmp
VOLUME /tmp

# Copies the different layers from the first image named build
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application
ENTRYPOINT ["java","-cp","app:app/lib/*","com.proto.greet.GreetingServiceApplication"]