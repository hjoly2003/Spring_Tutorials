# Distributed version of the Spring PetClinic Sample Application built with Spring Cloud 

## Status

Requires to start a `socat TCP-L:2375,fork UNIX:/var/run/docker.sock` process to enable the `docker-maven-plugin` to succeed.
When compiling (in the same terminal where $DOCKER_HOST is defined) with `./mvnw clean install -P buildDocker` in the root directory:
* Test failed in the *spring-petclinic-discovery-server*
```bash
Request execution error. endpoint=DefaultEndpoint{ serviceUrl='http://localhost:8761/eureka/}, exception=java.net.ConnectException: Connection refused
```
* Test failed in the *spring-petclinic-api-gateway*
```bash
Unable to load io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider, fallback to system defaults.
nested exception is io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: localhost/127.0.0.1:9411
```

However, the build succeeds. Then in another terminal at the root of the project, `docker-compose up` succeeds.

Also works with the `../mvnw spring-boot:run` command. 

TODO - What's the difference between the API-Gateway and the *Discovery Service*?
  

[![Build Status](https://github.com/spring-petclinic/spring-petclinic-microservices/actions/workflows/maven-build.yml/badge.svg)](https://github.com/spring-petclinic/spring-petclinic-microservices/actions/workflows/maven-build.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

This microservices branch was initially derived from [AngularJS version](https://github.com/spring-petclinic/spring-petclinic-angular1) to demonstrate how to split sample Spring application into [microservices](http://www.martinfowler.com/articles/microservices.html).
To achieve that goal, we use Spring Cloud Gateway, Spring Cloud Circuit Breaker, Spring Cloud Config, Spring Cloud Sleuth, Resilience4j, Micrometer 
and the Eureka Service Discovery from the [Spring Cloud Netflix](https://github.com/spring-cloud/spring-cloud-netflix) technology stack.


## Summary of the Microservices

* Uses Docker ([N]:docker) to deploy the applications into components.

* A Spring Cloud Config Server ([N]:config]:{server|client})(via the *Spring Cloud Configuration Server*) that is deployed as Docker container and can manage a services configuration information using    
  * a file system/ classpath ([N]:file-repo) OR
  * a GitHub-based repository ([N]:git-repo)
    TODO - Need to make a local git repository for the config files so that I'll be able to document further their content.

  The config clients are the Admin Server, the API-Gateway, the Customers Service, Discovery Server, the Vets Service and the Visits Service (see https://github.com/spring-petclinic/spring-petclinic-microservices-config). Only the Vets Service has properties that its business logic needs to be read (see VetsProperties.java) 

* Uses Spring Boot Actuator ([N]:actuator) to get access to configuration of a few services (API-Gateway, Customers Service, Vets Service and Visits Service).

* A Postgres SQL database used to hold the data ([N]:jpa) for Customers Service, Vets Service, Pets Service and Visits Service. For configuration, see the `spring.datasource` properties in [application.yml](https://github.com/spring-petclinic/spring-petclinic-microservices-config/blob/main/application.yml).
  TODO - Read about mysql configuration and see why we don't need to configure a db user and password.

* An *Eureka Server* ([N]:eureka-server) to facilitate inter-services communication. It is an application of the [Server-side service Discovery pattern](https://microservices.io/patterns/server-side-discovery.html) in which this *Router*  will allow *Service Instances* to register with its *Eureka Registry* by their  `eureka.instance.instance_id` property (for e.g. see the [visits-service.yml](https://github.com/spring-petclinic/spring-petclinic-microservices-config/blob/main/visits-service.yml)).  

  ![Pattern: Server-side discovery](./docs/server-side-discovery-pattern.jpg)

  For the Pet Clinit example, the *Eureka Server* configuration is defined in its [discovery-server.yml](https://github.com/spring-petclinic/spring-petclinic-microservices-config/blob/main/discovery-server.yml) and the *Registered Services* are the Admin server[?], the API-Gateway[?], the Customers Service and Visits Service.  

  * *Service Clients* ([N]:eureka-client) that need to call a *Registered Service* will use Eureka to lookup the physical location of the target service. A client accesses the location of the *Eureka Server* by the `eureka.client.serviceUrl.defaultZone` property in its config file (for e.g. see [customers-service.yml](https://github.com/spring-petclinic/spring-petclinic-microservices-config/blob/main/customers-service.yml)). For the Pet Clinit example, the *Service Clients* are the Admin Server[?], the API-Gateway, the Customers Service[?], the Vets Service[?] and the Visits Service[?].
    TODO - In the pom.xml of the *Service Clients*, there is a dependency on `spring-cloud-starter-netflix-eureka-client`, a library that I thought was only required for the *Registered Services*.

  * In combination with Eureka, we'll use client-side load balancing ([N]:load-balance) to cache the *Eureka registry* locally for lessening the load on the *Eureka Server* and improve client stability if Eureka becomes unavailable. For the client-side load balancing, we’ll use the Spring Cloud Load Balancer via `Spring Discovery Client` ([N]:eureka-client]:discovery). To call a *Registered Service*, we're directly instantiating a `WebClient` that is Load Balancer-aware (see [Spring WebClient as a Load Balancer Client](https://docs.spring.io/spring-cloud-commons/docs/3.1.1/reference/html/#webclinet-loadbalancer-client)).
    TODO - For an eureka-client using the @EnableDiscoveryClient annotation, we can't find any usage of the `DiscoveryClient`, unlike what's used in "Microservices In Action" chapter 6 (see OrganizationDiscoveryClient).

* *Resilience4j Circuit-breaker* ([N]:circuit) to fail fast on a slow service-call (see [Getting started with resilience4j-circuitbreaker](https://resilience4j.readme.io/docs/circuitbreaker)).


## Starting services locally without Docker

Every microservice is a Spring Boot application and can be started locally using IDE ([Lombok](https://projectlombok.org/) plugin has to be set up) or `../mvnw spring-boot:run` command. Please note that supporting services (Config and Discovery server) must be started before any other application (Customers, Vets, Visits and API).
Startup of Tracing server, Admin server, Grafana and Prometheus is optional.
If everything goes well, you can access the following services at given location:
* Discovery Server (Spring Eureka) - http://localhost:8761
* Config Server - http://localhost:8888
* AngularJS frontend (API Gateway) - http://localhost:8080
* Customers, Vets and Visits Services - random port, check Eureka Dashboard 
* Tracing Server (Zipkin) - http://localhost:9411/zipkin/ (we use [openzipkin](https://github.com/openzipkin/zipkin/tree/master/zipkin-server))
* Admin Server (Spring Boot Admin) - http://localhost:9090
* Grafana Dashboards - http://localhost:3000
* Prometheus - http://localhost:9091

You can tell Config Server to use your local Git repository by using `native` Spring profile and setting
`GIT_REPO` environment variable, for example:
`-Dspring.profiles.active=native -DGIT_REPO=/projects/spring-petclinic-microservices-config`

## Starting services locally with docker-compose
In order to start entire infrastructure using Docker, you have to build images by executing `./mvnw clean install -P buildDocker` 
from a project root. Once images are ready, you can start them with a single command
`docker-compose up`. Containers startup order is coordinated with [`dockerize` script](https://github.com/jwilder/dockerize) (<u>TODO [?]: dockerize script</u>). 
After starting services, it takes a while for API Gateway to be in sync with service registry,
so don't be scared of initial Spring Cloud Gateway timeouts. You can track services availability using Eureka dashboard
available by default at http://localhost:8761.

The `master` branch uses an  Alpine linux  with JRE 8 as Docker base. You will find a Java 11 version in the `release/java11` branch.

*NOTE: Under MacOSX or Windows, make sure that the Docker VM has enough memory to run the microservices. The default settings
are usually not enough and make the `docker-compose up` painfully slow.*


## Starting services locally with docker-compose and Java
If you experience issues with running the system via docker-compose you can try running the `./scripts/run_all.sh` script that will start the infrastructure services via docker-compose and all the Java based applications via standard `nohup java -jar ...` command. The logs will be available under `${ROOT}/target/nameoftheapp.log`. 

Each of the java based applications is started with the `chaos-monkey` profile in order to interact with Spring Boot Chaos Monkey. You can check out the (README)[scripts/chaos/README.md] for more information about how to use the `./scripts/chaos/call_chaos.sh` helper script to enable assaults.

## Understanding the Spring Petclinic application

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

[A blog post introducing the Spring Petclinic Microsevices](http://javaetmoi.com/2018/10/architecture-microservices-avec-spring-cloud/) (french language)

You can then access petclinic here: http://localhost:8080/

![Spring Petclinic Microservices screenshot](docs/application-screenshot.png)


**Architecture diagram of the Spring Petclinic Microservices**

![Spring Petclinic Microservices architecture](docs/microservices-architecture-diagram.jpg)


## In case you find a bug/suggested improvement for Spring Petclinic Microservices

Our issue tracker is available here: https://github.com/spring-petclinic/spring-petclinic-microservices/issues

## Database configuration

In its default configuration, Petclinic uses an in-memory database (HSQLDB) which gets populated at startup with data.
A similar setup is provided for MySql in case a persistent database configuration is needed.
Dependency for Connector/J, the MySQL JDBC driver is already included in the `pom.xml` files.

### Start a MySql database

You may start a MySql database with docker:

```
docker run -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8
```
or download and install the MySQL database (e.g., MySQL Community Server 5.7 GA), which can be found here: https://dev.mysql.com/downloads/

### Use the Spring 'mysql' profile

To use a MySQL database, you have to start 3 microservices (`visits-service`, `customers-service` and `vets-services`)
with the `mysql` Spring profile. Add the `--spring.profiles.active=mysql` as programm argument.
```bash
# [?]:TODO - Haven't tried yet.
$ mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

By default, at startup, database schema will be created and data will be populated.
You may also manually create the PetClinic database and data by executing the `"db/mysql/{schema,data}.sql"` scripts of each 3 microservices. 
In the `application.yml` of the [Configuration repository], set the `initialization-mode` to `never`.

If you are running the microservices with Docker, you have to add the `mysql` profile into the (Dockerfile)[docker/Dockerfile]:
```
ENV SPRING_PROFILES_ACTIVE docker,mysql
```
In the `mysql section` of the `application.yml` from the [Configuration repository], you have to change 
the host and port of your MySQL JDBC connection string. 

## Custom metrics monitoring

Grafana and Prometheus are included in the `docker-compose.yml` configuration, and the public facing applications
have been instrumented with [MicroMeter](https://micrometer.io) to collect JVM and custom business metrics.

A JMeter load testing script is available to stress the application and generate metrics: [petclinic_test_plan.jmx](spring-petclinic-api-gateway/src/test/jmeter/petclinic_test_plan.jmx)

![Grafana metrics dashboard](docs/grafana-custom-metrics-dashboard.png)

### Using Prometheus

* Prometheus can be accessed from your local machine at http://localhost:9091

### Using Grafana with Prometheus

* An anonymous access and a Prometheus datasource are setup.
* A `Spring Petclinic Metrics` Dashboard is available at the URL http://localhost:3000/d/69JXeR0iw/spring-petclinic-metrics.
You will find the JSON configuration file here: [docker/grafana/dashboards/grafana-petclinic-dashboard.json]().
* You may create your own dashboard or import the [Micrometer/SpringBoot dashboard](https://grafana.com/dashboards/4701) via the Import Dashboard menu item.
The id for this dashboard is `4701`.

### Custom metrics
Spring Boot registers a lot number of core metrics: JVM, CPU, Tomcat, Logback... 
The Spring Boot auto-configuration enables the instrumentation of requests handled by Spring MVC.
All those three REST controllers `OwnerResource`, `PetResource` and `VisitResource` have been instrumented by the `@Timed` Micrometer annotation at class level.

* `customers-service` application has the following custom metrics enabled:
  * @Timed: `petclinic.owner`
  * @Timed: `petclinic.pet`
* `visits-service` application has the following custom metrics enabled:
  * @Timed: `petclinic.visit`

## Looking for something in particular?

| Spring Cloud components         | Resources  |
|---------------------------------|------------|
| Configuration server            | [Config server properties](spring-petclinic-config-server/src/main/resources/application.yml) and [Configuration repository] |
| Service Discovery               | [Eureka server](spring-petclinic-discovery-server) and [Service discovery client](spring-petclinic-vets-service/src/main/java/org/springframework/samples/petclinic/vets/VetsServiceApplication.java) |
| API Gateway                     | [Spring Cloud Gateway starter](spring-petclinic-api-gateway/pom.xml) and [Routing configuration](/spring-petclinic-api-gateway/src/main/resources/application.yml) |
| Docker Compose                  | [Spring Boot with Docker guide](https://spring.io/guides/gs/spring-boot-docker/) and [docker-compose file](docker-compose.yml) |
| Circuit Breaker                 | [Resilience4j fallback method](spring-petclinic-api-gateway/src/main/java/org/springframework/samples/petclinic/api/boundary/web/ApiGatewayController.java)  |
| Grafana / Prometheus Monitoring | [Micrometer implementation](https://micrometer.io/), [Spring Boot Actuator Production Ready Metrics] |

 Front-end module  | Files |
|-------------------|-------|
| Node and NPM      | [The frontend-maven-plugin plugin downloads/installs Node and NPM locally then runs Bower and Gulp](spring-petclinic-ui/pom.xml)  |
| Bower             | [JavaScript libraries are defined by the manifest file bower.json](spring-petclinic-ui/bower.json)  |
| Gulp              | [Tasks automated by Gulp: minify CSS and JS, generate CSS from LESS, copy other static resources](spring-petclinic-ui/gulpfile.js)  |
| Angular JS        | [app.js, controllers and templates](spring-petclinic-ui/src/scripts/)  |


## Interesting Spring Petclinic forks

The Spring Petclinic `main` branch in the main [spring-projects](https://github.com/spring-projects/spring-petclinic)
GitHub org is the "canonical" implementation, currently based on Spring Boot and Thymeleaf.

This [spring-petclinic-microservices](https://github.com/spring-petclinic/spring-petclinic-microservices/) project is one of the [several forks](https://spring-petclinic.github.io/docs/forks.html) 
hosted in a special GitHub org: [spring-petclinic](https://github.com/spring-petclinic).
If you have a special interest in a different technology stack
that could be used to implement the Pet Clinic then please join the community there.


# Contributing

The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-microservices/issues) is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the [editor config](.editorconfig) for easy use in common text editors. Read more and download plugins at <http://editorconfig.org>.


[Configuration repository]: https://github.com/spring-petclinic/spring-petclinic-microservices-config
[Spring Boot Actuator Production Ready Metrics]: https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-metrics.html
