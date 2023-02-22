package com.rezz.model.audit;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateAuditEvent {
    private UUID transactionId;
    private String status;
}
