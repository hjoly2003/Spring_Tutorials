package com.rest.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Converts {@code Employee} objects to {@code EntityModel<Employee>} objects.<p>
 */
@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee,EntityModel<Employee>> {

    /**
     * Converts a non-model object ({@code Employee}) into a model-based object ({@code EntityModel<Employee>}).
     */
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).getEmployees()).withRel("employees"));
    }
}
