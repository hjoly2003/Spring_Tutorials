package com.circuitbreaker;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.circuitbreaker.model.Product;
import com.circuitbreaker.repository.ProductRepository;
import com.circuitbreaker.services.ProductSearchService;
import com.circuitbreaker.services.delays.NSecondsDelay;
import com.circuitbreaker.services.failures.GuaranteeFailure;
import com.circuitbreaker.services.failures.SucceedNTimesThenFail;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;

public class App
{
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    void my_circuit_breaker_implemntation() {
    	// add code here
        CircuitBreakerConfig config = CircuitBreakerConfig
            .custom()
    
            // // A - Failure with no delay. Count-based sliding window of 10 calls with a failure rate of 70%.
            // .slidingWindowType(SlidingWindowType.COUNT_BASED)
            // .slidingWindowSize(10)
            // .failureRateThreshold(70.0f)

            // // B – Slow calls with failures scenario. Count-based sliding window of 10 calls with a failure rate of 70% and a service delay of 2 seconds.
            // .slidingWindowType(SlidingWindowType.COUNT_BASED)
            // .slidingWindowSize(5)
            // .slowCallRateThreshold(70.0f)
            // .slowCallDurationThreshold(Duration.ofSeconds(2))
            
            // B – Slow calls with failures scenario. Count-based sliding window of 10 calls with a failure rate of 70% and a service delay of 2 seconds.
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(5)
            .failureRateThreshold(20.0f)

            // .slidingWindowType(SlidingWindowType.TIME_BASED)
            // .minimumNumberOfCalls(10)
            // .slidingWindowSize(10)
            // .slowCallRateThreshold(70.0f)
            // .slowCallDurationThreshold(Duration.ofSeconds(1))
            // .writableStackTraceEnabled(false)

            .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker(ProductSearchService.CB_NAME);

        ProductSearchService service = new ProductSearchService();

        // // A - Failure with no delay
        // service.setPotentialFailure(new SucceedNTimesThenFail(3));

        // B – Slow calls with failures scenario.
        service.setPotentialDelay(new NSecondsDelay(4));

        // B – Slow calls with failures scenario.
        service.setPotentialFailure(new GuaranteeFailure());;

        // service.setPotentialDelay(new NSecondsDelay(1));

        // To execute the product search call method as a lambda expression – a Supplier of List<Product>.
        Supplier<List<Product>> productsSupplier = () -> service.searchProducts(300);
        
        // Decorate the Supplier with the circuit breaker functionality.
        Supplier<List<Product>> decoratedProductsSupplier = Decorators
            .ofSupplier(productsSupplier)
            .withCircuitBreaker(circuitBreaker)
            .withFallback(Arrays.asList(CallNotPermittedException.class),
                        e -> this.getProductSearchResultsFromCache(300))
            .decorate();

        displaySearchResult(decoratedProductsSupplier);
    }
    


    private List<Product> getProductSearchResultsFromCache(int qty) {
    	List<Product> products = new ProductRepository().findByQuantity(qty);
        System.out.println("----Returning product search results from the cache----");
        return products;
    }

	/**
     * Displays results 20 times.
     * @param decoratedProductsSupplier
     */
    private void displaySearchResult(Supplier<List<Product>> decoratedProductsSupplier) {
		for (int i=0; i<20; i++) {
            try {
                System.out.println("Count: " + i + ", " + decoratedProductsSupplier.get());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
    public static void main( String[] args )
    {
        App app = new App();
        app.my_circuit_breaker_implemntation();
    }
}