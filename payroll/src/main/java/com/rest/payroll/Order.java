package com.rest.payroll;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@Entity
@Table(name = "CUSTOMER_ORDER")
@NoArgsConstructor
@Data
public class Order {

    @Id @GeneratedValue
    private Long id;
    private String description;
    private Status status;

    Order(String description, Status status) {
        this.description = description;
        this.status = status;
    }

    /**
     * [me] This method is used to update the status of an order.<p>
     * A business rule is enforced here: an order can only be cancelled or completed if it is in progress.
     * @param status
     */
    void setStatus(Status status) {

        EnumSet<Status> permittedStatus = EnumSet.of(Status.CANCELLED, Status.COMPLETED);

        if (!permittedStatus.contains(status) || this.status != Status.IN_PROGRESS) {
            throw new IllegalStateException("You can't set an order to a \"" + status + "\" state if it is in the " + this.status + " status.");
        }
        this.status = status;
    }
}
