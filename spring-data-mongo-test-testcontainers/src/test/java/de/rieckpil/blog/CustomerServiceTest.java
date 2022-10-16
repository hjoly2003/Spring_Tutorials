package de.rieckpil.blog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * [N]:testcontainers]:nosql]:mongodb - In the {@code @DataMongoTest} annotation, the {@code excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class} disables the spring-boot auto-configuration for an embedded MongoDB.<p/>
 * [N]:testcontainers - The Testcontainers JUnit Jupiter extension that we register with {@code @Testcontainers} takes care of the container lifecycle (starting/stopping).<p/>
disables */
@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
class CustomerServiceTest extends MongoDbTestBase {

  // @Container
  // static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

  // /**
  //  * Overrides the {@code spring.data.mongodb.uri} to point to the local database container (Testcontainers exposes a random ephemeral port)
  //  * @param registry
  //  */
  // @DynamicPropertySource
  // static void setProperties(DynamicPropertyRegistry registry) {
  //   // registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  //   registry.add("spring.data.mongodb.host", mongoDBContainer::getContainerIpAddress);
  //   registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
  //   registry.add("spring.data.mongodb.database", () -> "product-db");
  // }

  @Autowired
  private MongoTemplate mongoTemplate;

  private CustomerService cut;

  /** 
   * [N]:nosql]:mongodb - We have to instantiate the class under test on our own as it is not part of the Spring Test Context. */
  @BeforeEach
  void setUp() {
    this.cut = new CustomerService(mongoTemplate);
  }

  @Test
  void shouldReturnCustomersWithRatingGreater90AsVIP() {
    this.mongoTemplate.save(new Customer("duke@spring.io", 42));
    this.mongoTemplate.save(new Customer("mike@spring.io", 91));
    this.mongoTemplate.save(new Customer("hannah@spring.io", 99));

    List<Customer> result = cut.findAllVIPCustomers();

    assertEquals(2, result.size());
  }
}
