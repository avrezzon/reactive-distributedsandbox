package com.rezz.model.user;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class CreateUserResponse {

    public CreateUserResponse(){}

    private int userId;
    private int accountId;
    private String accountType;
    private Double accountBalance;

}
