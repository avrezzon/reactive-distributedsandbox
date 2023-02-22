package com.rezz.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplication {
    private UUID ownerId;
    private Double loanAmount;
    private Integer loanTerm;
}
