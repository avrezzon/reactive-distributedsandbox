package com.rezz.userservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(value = "USERS")
public class User {

    @Transient
    public static final User NO_OP = new User();

    @Id
    private UUID id;
    private String firstname;
    private String lastname;
    private String ssn;
}
