package com.rezz.accountservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(value = "ACCOUNT")
public class Account {

    @Id
    private Integer id;
    @Column("owner_id")
    private UUID ownerId;
    @Column("account_type")
    private String accountType;
    private Double balance;

}
