package com.rezz.errorservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(value = "ERROR_HISTORY")
public class ErrorEvent {

    @Id
    private UUID id;
    @Column("app_name")
    private String appName;
    @Column("endpoint")
    private String endpoint;
    @Column("payload")
    private String payload;
    @Column("message")
    private String message;
    @Column("status_code")
    private Integer statusCode;
    @Column("transaction_id")
    private String transactionId;
    @Column("event_timestamp")
    private LocalDateTime eventTimestamp;



}
