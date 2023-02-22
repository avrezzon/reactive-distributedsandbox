package com.rezz.userservice.handler;

import com.rezz.exception.ResourceNotFoundException;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailedTransactionResponse> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<>(new FailedTransactionResponse("Client", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailedTransactionResponse> handleResourceNotFoundException(ResourceNotFoundException e){
        return new ResponseEntity<>(new FailedTransactionResponse("Client", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedTransactionException.class)
    public ResponseEntity<FailedTransactionResponse> handleFailedTransactionException(FailedTransactionException e){
        HttpStatus statusCodeToCaller = (e.getOriginalStatusCode().is5xxServerError()) ? HttpStatus.CONFLICT : e.getOriginalStatusCode();
        return new ResponseEntity<>(new FailedTransactionResponse(e), statusCodeToCaller);
    }
}
