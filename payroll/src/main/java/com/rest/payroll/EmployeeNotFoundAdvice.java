package com.rest.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Used to render an HTTP 404 response when an {@code EmployeeNotFoundException} is thrown.<p>
 * <ul>
 *  <li>{@code @ResponseBody} signals that this advice is rendered straight into the response body.</li>
 *  <li>{@code @ExceptionHandler} configures the advice to only respond if an EmployeeNotFoundException is thrown.</li>
 *  <li>{@code @ResponseStatus} says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.</li>
 *  <li>The body of the advice generates the content. In this case, it gives the message of the exception.</li>
 * </ul>
 */
@ControllerAdvice
public class EmployeeNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex) {
        return ex.getMessage();
    }
}
