package com.rezz.model.account;

import lombok.Data;

import javax.validation.constraints.Positive;

@Data
public class TransferRequest {
    private int toAccount;
    private int fromAccount;
    @Positive
    private Double amount;
}
