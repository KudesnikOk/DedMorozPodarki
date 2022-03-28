package dev.azonov.giftservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.azonov.giftservice.exceptions.InvalidOperationException;
import lombok.Data;

/**
 * API model for gift kind
 */

@Data
public class GiftModel {
    @JsonIgnore
    private Long id;
    private Integer quantity;
    private String kind;

    /**
     * Reduce quantity of gifts by one
     */
    public void reduceQuantity() {
        if (quantity <= 0) {
            throw new InvalidOperationException("Can not reduce quantity because it is already non positive");
        }

        quantity--;
    }

    /**
     * Increase increment of gifts by given amount
     * @param increment amount of produced gifts
     */
    public void increaseQuantity(int increment) {
        if (increment <= 0) {
            throw new InvalidOperationException("Can not increase quantity, because increment is not positive");
        }

        this.quantity += increment;
    }
}
