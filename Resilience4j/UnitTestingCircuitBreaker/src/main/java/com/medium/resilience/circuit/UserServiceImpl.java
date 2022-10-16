package com.medium.resilience.circuit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserServiceImpl implements UserService {
	public static final String CB_USERS_REPOSITORY = "UsersRepositoryCircuitBreaker";

	/* [N]:jpa */
	@Autowired
	private UserRepository userRepository;

	/**
	 * [N]:circuit - 
	 */
	@Override
	@CircuitBreaker(name = CB_USERS_REPOSITORY)
	@Retry(name = CB_USERS_REPOSITORY)
	public String getName(Integer userId) {
		return userRepository.findById(userId).map(User::getName).orElse("<id-not-found>");
	}
}