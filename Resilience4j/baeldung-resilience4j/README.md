## Status
Works after having done the following modifications:
* Added a spring-boot-starter-web dependency or else Visual Studio Code eats the content of the resilience4j jars thinking that they aren't required.
* Has modified the bulkhead test to verify that a BulkheadFullException is thrown as expected.

## Origin

* [Guide to Resilience4j | Baeldung](https://www.baeldung.com/resilience4j)



