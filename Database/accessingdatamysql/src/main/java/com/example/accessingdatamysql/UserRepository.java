package com.example.accessingdatamysql;

import org.springframework.data.repository.CrudRepository;

/**
 * This will be AUTO IMPLEMENTED by Spring intoa Bean called userRepository
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    
}
