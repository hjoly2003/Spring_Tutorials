package de.rieckpil.blog;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * [N]:nosql]:mongodb - For MongoRepository, the first template argument is the entity class and the second argument is the type of the key in that entity class ({@code Customer.id}).
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {

  @Query(sort = "{ rating : 1 }")
  List<Customer> findByRatingBetween(int from, int to);
}
