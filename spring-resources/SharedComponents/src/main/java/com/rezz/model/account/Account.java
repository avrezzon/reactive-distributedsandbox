package com.rezz.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Integer id;
    private UUID ownerId;
    private String accountType;
    private Double balance;

}
