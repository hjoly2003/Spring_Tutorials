package com.baeldung.resilence4j;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class Resilence4jApplicationTests {

	interface RemoteService {
		int process(int i);
	}

	private RemoteService service;

	@BeforeEach
	public void setUp() {
		service = mock(RemoteService.class);
	}

	/**
	 * [N]:resilience-circuit - Circuit breaker: After a number of failed attempts, it considers that the service is unavailable/overloaded and the circuit breaker eagerly rejects all subsequent requests to it. In this way, we can save system resources for calls which are likely to fail.
	 */
	@Test
	void whenCircuitBreakerIsUsed_thenItWorksAsExpected() {

		// For establishing the contitions under which the circuit breaker falls into open state.
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
											// [N]:resilience-circuit - A failure rate of 20%
											.failureRateThreshold(20)
											.slidingWindowType(SlidingWindowType.COUNT_BASED)
											// [N]:resilience-circuit - It takes 5 calls for a diagnostic
											.slidingWindowSize(5)
											.build();

		CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
		CircuitBreaker circuitBreaker = registry.circuitBreaker("my");
		Function<Integer, Integer> decorated = CircuitBreaker.decorateFunction(circuitBreaker, service::process);

		when(service.process(anyInt())).thenThrow(new RuntimeException());

		for (int i = 0; i < 10; i++) {
			try {
				decorated.apply(i);
			} catch (Exception e) {}
		}
		// To verify that the call was attempted a minimum of 5 times, then stopped as soon as 20% of calls failed.
		verify(service, times(5)).process(any(Integer.class));
	}

	/**
	 * [N]:resilience-rate - A Rate Limiter blocks calls to some service if the calls rate is above its configuration.
	@Test
	void whenRateLimiterIsUsed_thenItWorksAsExpected() {
		
		RateLimiterConfig config = RateLimiterConfig.custom().limitForPeriod(2).build();
		RateLimiterRegistry registry = RateLimiterRegistry.of(config);
		RateLimiter rateLimiter = registry.rateLimiter("my");
		Function<Integer, Integer> decorated = RateLimiter.decorateFunction(rateLimiter, service::process);
	}
	*/

	/**
	 * [N]:resilience-retry - To automatically retry a failed call.
	 */
	@Test
	public void whenRetryIsUsed_thenItWorksAsExpected() {
		RetryConfig config = RetryConfig.custom().maxAttempts(2).build();
		RetryRegistry registry = RetryRegistry.of(config);
		Retry retry = registry.retry("my");

		Function<Integer, Void> decorated = Retry.decorateFunction(retry, (Integer s) -> {
			service.process(s);
			return null;
		});

		// Emulates a situation where an exception is thrown during a remote service call.
		when(service.process(anyInt())).thenThrow(new RuntimeException());
		try {
			decorated.apply(1);
			fail("Expected an exception to be thrown if all retries failed");
		} catch (Exception e) {
			// Ensures that the library automatically retries the failed call.
			verify(service, times(2)).process(any(Integer.class));
		}
	}

	/**
	 * [N]:resilience-timeLimiter - To limit the amount of time spent calling a remote service.
	 * @throws Exception
	 */
	@Test
	public void whenTimeLimiterIsUsed_thenItWorksAsExpected() throws Exception {
		long ttl = 1;
		TimeLimiterConfig config = TimeLimiterConfig.custom().timeoutDuration(Duration.ofMillis(ttl)).build();
		TimeLimiter timeLimiter = TimeLimiter.of(config);

		var futureMock = mock(Future.class);
		var restrictedCall = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futureMock);
		restrictedCall.call();

		// Note: we could combine it with CircuitBreaker like so:
		//Callable chainedCalllable = CircuitBreaker.decorateCallable(circuitBreaker, restrictedCall);

		// To verify that Resilience4j calls Future.get() with the expected timeout.
		verify(futureMock).get(ttl, TimeUnit.MILLISECONDS);
	}

	/**
	 * [N]:resilience-bulkhead - A Bulkhead limits the number of concurrent calls to a particular service.
	 * @throws InterruptedException
	 */
	@Test
	public void whenBulkheadIsUsed_thenItWorksAsExpected() throws InterruptedException {

		BulkheadConfig config = BulkheadConfig.custom()
												.maxConcurrentCalls(1)
												.maxWaitDuration(Duration.ofSeconds(1))
												.build();
		BulkheadRegistry registry = BulkheadRegistry.of(config);
		Bulkhead bulkhead = registry.bulkhead("my");
		Function<Integer, Integer> decorated = Bulkhead.decorateFunction(bulkhead, service::process);

		// The 1st call is made and blocks to ensure that Bulkhead doesn't allow any other call.
		Future<?> taskInProgress = callAndBlock(decorated);

		try {
			bulkhead.acquirePermission();
		} catch (Exception e) {
			verify(service, times(1)).process(any(Integer.class));
		} finally {
			taskInProgress.cancel(true);
		}
	}

	private Future<?> callAndBlock(Function<Integer, Integer> decoratedService) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		when(service.process(anyInt())).thenAnswer(invocation -> {
			latch.countDown();
			Thread.currentThread().join();
			return null;
		});

		ForkJoinTask<?> result = ForkJoinPool.commonPool().submit(() -> {
			decoratedService.apply(1);
		});
		latch.await();
		return result;
	}

}
