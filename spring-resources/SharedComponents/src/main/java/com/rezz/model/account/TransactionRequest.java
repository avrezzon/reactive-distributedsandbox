package com.rezz.model.account;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class TransactionRequest {

    private int accountNumber;

    @Positive
    private Double amount;

}
