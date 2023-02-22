package com.rezz.orchestrationservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    private String firstname;
    private String lastname;
    private String ssn;
    private Double savingsAmount;

}
