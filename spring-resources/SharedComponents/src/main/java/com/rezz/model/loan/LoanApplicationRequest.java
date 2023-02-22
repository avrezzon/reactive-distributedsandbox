package com.rezz.model.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequest {
    private Integer loanTerm;
    private Double loanAmount;
    private Integer creditRating;
    private String ssn;
}
