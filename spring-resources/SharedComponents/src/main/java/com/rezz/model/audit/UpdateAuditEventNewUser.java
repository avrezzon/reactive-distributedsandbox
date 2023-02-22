package com.rezz.model.audit;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateAuditEventNewUser {
    private UUID transactionId;
    private String status;
    private int userId;
    private int accountId;
}
