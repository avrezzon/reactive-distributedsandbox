package com.rezz.accountservice.handler;

import com.rezz.exception.ResourceNotFoundException;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailedTransactionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new FailedTransactionResponse("Client", e.getMessage()), HttpStatus.NOT_FOUND);
    }
}

