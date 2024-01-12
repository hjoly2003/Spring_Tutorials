package com.rest.payroll;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Used to render an HTTP 404 response when an {@code EmployeeNotFoundException} is thrown.<p>
     * <ul>
     *  <li>{@code @ResponseBody} signals that this advice is rendered straight into the response body.</li>
     *  <li>{@code @ExceptionHandler} configures the advice to only respond if an EmployeeNotFoundException is thrown.</li>
     *  <li>{@code @ResponseStatus} says to issue an {@code HttpStatus.NOT_FOUND}, i.e. an HTTP 404.</li>
     *  <li>The body of the advice generates the content. In this case, it gives the message of the exception.</li>
     * </ul>
     */
    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * [me] Used to render an HTTP 404 response when an {@code OrderNotFoundException} is thrown.<p>
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    ResponseEntity<?> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
            .body(Problem.create()
                .withTitle("Method not allowed")
                .withDetail(ex.getMessage()));
    }


}