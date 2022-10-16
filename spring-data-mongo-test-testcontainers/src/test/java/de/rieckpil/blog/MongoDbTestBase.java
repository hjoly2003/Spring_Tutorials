package de.rieckpil.blog;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

/**
 * [N]:nosql]:mongodb]:testcontainers - A base class for persistence testing that is used to launch a single Docker container for MongoDB.<p/>
 * It is used as a TestContainer (https://www.testcontainers.org) to simplify the running of automated test by starting resource managers like a database as a Docker container when JUnit tests are started and tear down the containes when the tests are complete.  
 * @see <a href="https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers">Singleton containers</a>
 */
public abstract class MongoDbTestBase {

  // The version specified for MongoDB, 4.4.2, is copied from Docker Compose files to ensure that the same version is used.
  private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
  
  /**
   * [N]:nosql]:mongodb]:testcontainers - A static block is used to start the database container before any JUnit code is invoked.
   */
  static {
    mongoDBContainer.start();
  } 

  // /**
  //  * Overrides the {@code spring.data.mongodb.uri} to point to the local database container (Testcontainers exposes a random ephemeral port)
  //  * @param registry
  //  */
  // @DynamicPropertySource
  // static void setProperties(DynamicPropertyRegistry registry) {
  //   registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  // }
  
  /**
   * To register dynamically created properties in the application context at startup.<p/>
   * The {@code @DynamicPropertySource} annotation overrides the database configuration in the application context, such as the configuration from an application.yml file.
   * @param registry
   */
  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.host", mongoDBContainer::getContainerIpAddress);
    registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    registry.add("spring.data.mongodb.database", () -> "product-db");
  }
}
