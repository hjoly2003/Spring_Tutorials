# Codebase for the blog post [MongoDB Testcontainers Setup for @DataMongoTest](https://rieckpil.de/mongodb-testcontainers-setup-for-datamongotest/)

Steps to run this project:

1. Clone this Git repository
2. Navigate to the folder `spring-data-mongo-test-testcontainers`
3. Make sure your Docker engine is up and running (`docker ps`)
4. Run all tests with `mvn verify`

Code related to the topic is annotated with the following: `[N]:testcontainers`.

Note that you might have to remove unused containers and images from docker:
`$ docker system prune -a`
