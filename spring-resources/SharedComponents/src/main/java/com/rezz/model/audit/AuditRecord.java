package com.rezz.model.audit;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class AuditRecord {
    private UUID transactionUuid;
    private int userId;
    private String operation;
    private String status;
    private int account;
    private double amount;
    private int toAccount;
    private LocalDateTime eventTimestamp;
}
