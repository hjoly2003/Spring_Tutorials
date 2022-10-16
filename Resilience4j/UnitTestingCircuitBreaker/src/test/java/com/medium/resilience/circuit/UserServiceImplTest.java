package com.medium.resilience.circuit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.persistence.QueryTimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;

/** 
 * [N]:circuit - Use the {@code @SpringBootTest} annotation to build <em>Application Context<em/> in same way as in production. As a result, the {@code @CircuitBreaker} annotation on the Service Implementation is processed and Circuit Breaker is applied.<p/>
 * Use the {@code @EnableAutoConfiguration(exclude = } annotation to disable DataSource and related auto configuration so that DB connection is not established in the test Spring Data abstraction to access the DB (See how to <a href="https://www.baeldung.com/spring-boot-exclude-auto-configuration-test">Exclude Auto-Configuration Classes in Spring Boot Tests | Baeldung</a> and <a href="https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-auto-configuration-classes.html">auto-configuration classes</a>). <p/>
 * {@code UserRepository} is mocked for the purpose of this testing anyways.<p/>
 * [N]:circuit - Since CircuitBreaker state is shared between the threads and each of tests in this class relies on specific state, the {@code @Execution(ExecutionMode.SAME_THREAD)} annotation forbid to run tests in parallel.<p/>
*/
@SpringBootTest(classes = 	UnitTestingCircuitBreakersApplication.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, 
									DataSourceTransactionManagerAutoConfiguration.class, 
									HibernateJpaAutoConfiguration.class})
@Execution(ExecutionMode.SAME_THREAD)
class UserServiceImplTest {

	// Real UserService is wired in, as it's the main scope of our tests, @SpringBootTest annotation on the test class leverages Spring testing harness to build Application Context same way as in production. As a result @CircuitBreaker annotation on the Service Implementation is processed and Circuit Breaker is applied.
	@Autowired
	private UserService userService;

	// UserRepository is mocked to mimic successful or erroneous flows: connection timeouts, record not found, etc.
	@MockBean
	private UserRepository userRepository;

	// [N]:circuit - Real CircuitBreakerRegistry is wired in to be able to set.
	@Autowired
	private CircuitBreakerRegistry circuitBreakerRegistry;

	@Test
	public void whenCircuitBreakerIsUsed_thenItWorksAsExpected() {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
														// Percentage of failures to start short-circuit
														.failureRateThreshold(20)
														// Min number of call attempts
														.slidingWindow(5, 5, SlidingWindowType.COUNT_BASED)
														.build();
		CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
		registry.circuitBreaker(UserServiceImpl.CB_USERS_REPOSITORY).transitionToClosedState();

		when(userRepository.findById(eq(123))).thenThrow(new RuntimeException());

		for (int i = 0; i < 10; i++) {
			try {
				userService.getName(123);
			} catch (Exception ignore) {
			}
		}

		verify(userRepository, times(5)).findById(any(Integer.class));
	}

	/**
	 * [N]:circuit
	 */
	@Test
	public void serviceClientGetsNameWhenCircuitBreakerIsClosedAndBackendIsFunctional() {

		circuitBreakerRegistry.circuitBreaker(UserServiceImpl.CB_USERS_REPOSITORY).transitionToClosedState();

		when(userRepository.findById(eq(123))).thenReturn(Optional.of(new User(123, "Name")));

		// The test acts as a Service Client and calls the Service via the Service Interface. Service Client has no knowledge of Circuit Breaker as it's implementation detail of Service Implementation
		String name = userService.getName(123);

		assertEquals("Name", name);
		verify(userRepository, times(1)).findById(eq(123));
	}

	/**
	 * [N]:circuit
	 */
	@Test
	public void serviceClientGetsExceptionWhenCircuitBreakerIsClosedButBackendTimesOut() {

		circuitBreakerRegistry.circuitBreaker(UserServiceImpl.CB_USERS_REPOSITORY).transitionToClosedState();

		when(userRepository.findById(eq(123))).thenThrow(new QueryTimeoutException(""));

		try {
			// The test acts as a Service Client and calls the Service via the Service Interface. Service Client has no knowledge of Circuit Breaker as it's implementation detail of Service Implementation
			userService.getName(123);

			fail("Test scenario expects RuntimeException at this point");
		} catch (RuntimeException e) {
			assertEquals(QueryTimeoutException.class, e.getClass());
			verify(userRepository, times(1)).findById(eq(123));
		}
	}

	/**
	 * [N]:circuit
	 */
	@Test
	public void serviceClientGetsExceptionWhenCircuitBreakerIsOpen() {

		circuitBreakerRegistry.circuitBreaker(UserServiceImpl.CB_USERS_REPOSITORY).transitionToOpenState();

		when(userRepository.findById(eq(123))).thenThrow(new QueryTimeoutException(""));

		try {
			// The test acts as a Service Client and calls the Service via the Service Interface. Service Client has no knowledge of Circuit Breaker as it's implementation detail of Service Implementation. Service Client gets a RuntimeException (generic as per example interface contract) but... (see below)
			userService.getName(123);

			fail("Test scenario expects RuntimeException at this point");
		} catch (RuntimeException e) {
			// ...but Service Client can distinguish the underlying reason of the failed call
			assertEquals(CallNotPermittedException.class, e.getClass());
			verifyNoInteractions(userRepository);
		}
	}
}
