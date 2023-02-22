package com.rezz.errorservice.converter;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.rezz.errorservice.model.ErrorEvent;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ErrorEventConverter {

    private final Tracer tracer;

    public ErrorEvent createEvent(FailedTransactionResponse response) {
        return ErrorEvent.builder()
                .appName(response.getAppName())
                .endpoint(response.getEndpoint())
                .payload(response.getFailedPayload())
                .message(response.getMessage())
                .statusCode(response.getOriginalStatusCode().value())
                .transactionId(Optional.ofNullable(tracer)
                        .map(Tracer::currentSpan)
                        .map(Span::context)
                        .map(TraceContext::spanIdString)
                        .orElse(null))
                .build();
    }

}
