package com.baeldung.docker;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [N]:jpa - Extends {@code JpaRepository} where the 1st generic argument is the entity class and the 2nd generic argument is the type of its primary key (License.licenseId)<p/>
 */
public interface CustomerRepository  extends JpaRepository<Customer,Long> {
    
}
