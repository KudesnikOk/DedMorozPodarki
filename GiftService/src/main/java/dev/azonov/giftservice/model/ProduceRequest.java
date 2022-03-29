package dev.azonov.giftservice.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Request to produce gifts of given kind
 */
@Data
public class ProduceRequest {
    @NotBlank
    @Length(max = 50)
    private String kind;
}
