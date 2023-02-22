package com.rezz.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateAccountRequest {

    private UUID ownerId;

    @NotEmpty
    private String accountType;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 2)
    private Double startingBalance;
}
