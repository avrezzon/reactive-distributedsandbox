package com.rezz.restcommunications.exception;

import com.rezz.restcommunications.model.FailedTransactionResponse;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class FailedTransactionException extends RuntimeException {

    private final String appName;
    private final String endpoint;
    private final String failedPayload;
    private final HttpStatus originalStatusCode;

    // This is to be used when handling 5xxServerErrors
    public FailedTransactionException(String appName, String endpoint, String payload, Throwable throwable, HttpStatus originalStatusCode) {

        super(String.format("Failed when calling %s for endpoint %s, received HttpStatus code of %s", appName, endpoint, originalStatusCode), throwable);

        this.appName = appName;
        this.endpoint = endpoint;
        this.failedPayload = payload;
        this.originalStatusCode = originalStatusCode;
    }

    public FailedTransactionException(String appName, String endpoint, String payload, String message, HttpStatus originalStatusCode) {

        super(message);

        this.appName = appName;
        this.endpoint = endpoint;
        this.failedPayload = payload;
        this.originalStatusCode = originalStatusCode;
    }

    // This is to be used when handling 4xxClientErrors
    public FailedTransactionException(FailedTransactionResponse response){

        super(response.getMessage());

        this.appName = response.getAppName();
        this.endpoint = response.getEndpoint();
        this.failedPayload = response.getFailedPayload();
        this.originalStatusCode = response.getOriginalStatusCode();
    }

}
