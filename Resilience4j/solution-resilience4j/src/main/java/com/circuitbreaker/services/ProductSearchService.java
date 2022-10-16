package com.circuitbreaker.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.circuitbreaker.model.Product;
import com.circuitbreaker.repository.ProductRepository;
import com.circuitbreaker.services.delays.IDelay;
import com.circuitbreaker.services.delays.NoDelay;
import com.circuitbreaker.services.failures.IFailure;
import com.circuitbreaker.services.failures.NoFailure;

// import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

public class ProductSearchService {
	// By default, we are assuming there will be no failure and no delay involved when we search for products.
    IFailure potentialFailure = new NoFailure();
    IDelay potentialDelay = new NoDelay();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public static final String CB_NAME="productSearchService";

    // @CircuitBreaker(name=CB_NAME)
    public List<Product> searchProducts(int qty) {
        System.out.println("Searching for products; current time = " + LocalDateTime.now().format(formatter));
        potentialDelay.delay();
        potentialFailure.fail();

        ProductRepository repo = new ProductRepository();
        List<Product> result = repo.findByQuantity(qty);

        System.out.println("Product search successful");
        return result;
    }

    // these methods will be used to set potential failure and delay to test circuit breaker with various configurations.
    public void setPotentialFailure(IFailure potentialFailure) {
        this.potentialFailure = potentialFailure;
    }

    public void setPotentialDelay(IDelay potentialDelay) {
        this.potentialDelay = potentialDelay;
    }
}