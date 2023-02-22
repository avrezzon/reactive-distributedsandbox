package com.rezz.model.audit;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateAuditEvent {

    private int userId;
    private int accountId;
    private double amount;
    private String operation;
    private int toAccount;
}
