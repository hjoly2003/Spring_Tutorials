package com.rest.payroll;

import org.springframework.http.HttpStatus; 
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [me] maps this exception to an HTTP status code
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException {
    OrderNotFoundException(Long id) {
        super("Could not find order " + id + ".");
    }
}
