package com.rezz.accountservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Table("USERS")
public class User {

    @Transient
    public static final User NO_OP = new User();

    @Id
    private int id;
    private String firstname;
    private String lastname;
    private String ssn;


}
