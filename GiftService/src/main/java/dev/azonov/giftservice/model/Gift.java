package dev.azonov.giftservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.azonov.giftservice.exceptions.InvalidOperationException;
import lombok.Data;

/**
 * API model for gift kind
 */

@Data
public class Gift {
    @JsonIgnore
    private Long id;
    private Integer quantity;
    private String kind;

    public void reduceQuantity() {
        if (quantity <= 0) {
            throw new InvalidOperationException("Quantity is already non positive");
        }

        quantity--;
    }
}
