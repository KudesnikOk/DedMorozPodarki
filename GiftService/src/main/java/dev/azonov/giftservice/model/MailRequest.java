package dev.azonov.giftservice.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Gift request for a child
 */
@Data
public class MailRequest {
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Length(min = 2, max = 50)
    private String secondName;

    @Length(max = 50)
    private String middleName;

    @NotBlank
    @Length(max = 50)
    private String giftKind;
}
