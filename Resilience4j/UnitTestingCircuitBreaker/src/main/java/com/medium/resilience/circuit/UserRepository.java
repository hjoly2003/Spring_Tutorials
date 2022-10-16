package com.medium.resilience.circuit;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * [N]:jpa - Extends {@code JpaRepository} where the 1st generic argument is the entity class and the 2nd generic argument is the type of its primary key (User.id)<p/>
 */
public interface UserRepository  extends JpaRepository<User,Integer> {

}
