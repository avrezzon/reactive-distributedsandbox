package com.rezz.loanservice.handler;

import com.rezz.restcommunications.model.FailedTransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailedTransactionResponse> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<>(new FailedTransactionResponse("Client", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
