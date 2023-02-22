package com.rezz.restcommunications.model;

import com.rezz.restcommunications.exception.FailedTransactionException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailedTransactionResponse {

    private String appName;
    private String endpoint;
    private String failedPayload;
    private String message;
    private HttpStatus originalStatusCode;

    public FailedTransactionResponse(FailedTransactionException e) {
        this.appName = e.getAppName();
        this.endpoint = e.getEndpoint();
        this.failedPayload = e.getFailedPayload();
        this.message = Optional.ofNullable(e.getCause())
                .map(Throwable::getLocalizedMessage)
                .orElse(e.getMessage());
        this.originalStatusCode = e.getOriginalStatusCode();
    }

    public FailedTransactionResponse(String appName, String message){
        this.appName = appName;
        this.message = message;
    }

}
