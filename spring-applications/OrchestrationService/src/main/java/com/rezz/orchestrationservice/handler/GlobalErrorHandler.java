package com.rezz.orchestrationservice.handler;

import com.rezz.orchestrationservice.client.ErrorCommunication;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorHandler {

    private final ErrorCommunication errorCommunication;

    @ExceptionHandler(FailedTransactionException.class)
    public Mono<ResponseEntity<FailedTransactionResponse>> handleFailedTransactionException(FailedTransactionException e) {

        return Mono.just(e)
                .map(FailedTransactionResponse::new)
                .publishOn(Schedulers.boundedElastic())
                .map(o -> {
                    errorCommunication.createNewEvent(o).subscribe();
                    HttpStatus statusCodeToCaller = (e.getOriginalStatusCode().is5xxServerError()) ? HttpStatus.CONFLICT : e.getOriginalStatusCode();
                    return new ResponseEntity<FailedTransactionResponse>(o, statusCodeToCaller);
                });

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailedTransactionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(FailedTransactionResponse.builder()
                .appName("Client")
                .message(e.getMessage())
                .originalStatusCode(HttpStatus.BAD_REQUEST)
                .build(), HttpStatus.BAD_REQUEST);
    }


}
