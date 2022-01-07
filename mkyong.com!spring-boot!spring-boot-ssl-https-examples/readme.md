## What is it?
This source code is an Spring Boot SSL (HTTPS) example.

Tested with
* Maven 3
* Java 8
* Spring Boot 2.2.4.RELEASE
* Spring Boot default embedded Tomcat 9
* Self-signed certificate (PKCS12)

For explanation, please visit this article - [Spring Boot SSL (HTTPS) examples](https://mkyong.com/spring-boot/spring-boot-ssl-https-examples/). The certificate is created vial `keytool`.

See [Creating a Self-Signed SSL Certificate](https://linuxize.com/post/creating-a-self-signed-ssl-certificate/) for further explanations on how to do it with `openssl`.

## How to run this?
```bash
$ git clone https://github.com/mkyong/spring-boot

$ cd spring-boot-ssl

$ mvn clean package

$ java -jar target/spring-boot-web.jar

  access https://localhost:8443
```