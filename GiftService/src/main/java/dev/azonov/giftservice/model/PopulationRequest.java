package dev.azonov.giftservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopulationRequest {
    @Min(value = 1, message = "Increment should not be less than 1")
    @Max(value = 20, message = "Increment should not be greater than 20")
    private int increment;
}
