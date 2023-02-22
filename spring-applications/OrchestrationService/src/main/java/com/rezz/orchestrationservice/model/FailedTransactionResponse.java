package com.rezz.orchestrationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedTransactionResponse implements Serializable {

    private String failureLocation;
    private String reason;
    private String originalRequest;
    private String failedRequest;

    //TODO add JSON string of original payload and starting payload
    //STRING CLASS NAME

}
