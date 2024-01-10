package com.rest.payroll;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Converts {@code Order} objects to {@code EntityModel<Order>} objects.<p>
 */
@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    /**
     * Depending on the given {@code order}'s status the method returns the valid action-links that can be applicable to it.
     * @param order
     * @return the {@code EntityModel<Order>} corresponding to the given {@code order}.
     */
    @Override
    public EntityModel<Order> toModel(Order order) {

        // Unconditional links to single-item resource and aggregate root
        EntityModel<Order> orderModel = EntityModel.of(order,
            linkTo(methodOn(OrderController.class).getOrderById(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).getOrders()).withRel("orders"));

        // Conditional links based on state of the order
        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderModel;
    }

}
