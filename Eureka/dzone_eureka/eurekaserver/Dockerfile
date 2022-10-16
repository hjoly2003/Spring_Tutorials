FROM openjdk:11-slim as build

# Determines the application will be exposed to the world on port 8761
EXPOSE 8761
# ADD <Source path of jar from where Docker needs to create image> <Destination in Docker>
ADD /target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar
# Determines which Java jar file needs to be run.
ENTRYPOINT ["java","-jar","eurekaserver-0.0.1-SNAPSHOT.jar"]