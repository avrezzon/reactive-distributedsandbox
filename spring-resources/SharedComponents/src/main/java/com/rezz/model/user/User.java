package com.rezz.model.user;

import com.rezz.model.account.Account;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class User implements Serializable {

    private UUID id;
    private String firstname;
    private String lastname;
    private String ssn;
    private List<Account> accounts;

}
