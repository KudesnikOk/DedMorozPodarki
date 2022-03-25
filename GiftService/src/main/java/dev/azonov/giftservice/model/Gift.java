package dev.azonov.giftservice.model;

import lombok.Data;

/**
 * API model for gift kind
 */

@Data
public class Gift {
    private Integer quantity;
    private String kind;
}
