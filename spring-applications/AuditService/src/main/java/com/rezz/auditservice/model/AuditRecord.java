package com.rezz.auditservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(value = "TRANSACTIONS")
public class AuditRecord {

    @Id
    @NotEmpty
    @Column("transaction_uuid")
    private UUID transactionUuid;

    @Column("acting_user")
    private int userId;

    private String operation;
    private String status;
    private int account;
    private double amount;

    @Column("to_account")
    private int toAccount;

    @Column("event_timestamp")
    private LocalDateTime eventTimestamp;



}
