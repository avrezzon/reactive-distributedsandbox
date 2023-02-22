package com.rezz.restcommunications.webclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class WebClientUtils {

    private WebClientUtils() {
    }

    public static final ObjectMapper mapper = new ObjectMapper();

    public static Function<ClientResponse, Mono<? extends Throwable>> handle5xxServerError(String appName, String endpoint, Object payload) {
        return response -> {
            try {
                String jsonPayload = mapper.writeValueAsString(payload);
                return response.bodyToMono(HashMap.class).flatMap(error -> {
                    String message = (String) error.get("trace");
                    return Mono.error((new FailedTransactionException(appName, endpoint, jsonPayload, message, response.statusCode())));
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    //Given A -> B -> C
    // if C fails with a 5xx, B needs to supply the failed payload
    // if C fails with a 4xx, B needs to supply the failed payload if C didn't
    public static Function<ClientResponse, Mono<? extends Throwable>> handle4xxClientError(Object payload) {
        return response -> {
            try {
                String jsonPayload = mapper.writeValueAsString(payload);
                return response.bodyToMono(FailedTransactionResponse.class)
                        .flatMap(ftr -> {
                            ftr.setFailedPayload(Optional.of(ftr)
                                    .map(FailedTransactionResponse::getFailedPayload)
                                    .orElse(jsonPayload));
                            ftr.setOriginalStatusCode(Optional.of(ftr)
                                    .map(FailedTransactionResponse::getOriginalStatusCode)
                                    .orElse(response.statusCode()));
                            return Mono.error(new FailedTransactionException(ftr));
                        });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    //This method is to be used when A -> B -> C and the calling service is B
    // The intent is to update the response of Client to the offending service that failed to do validation
    public static Function<ClientResponse, Mono<? extends Throwable>> handle4xxClientError(String appName, Object payload) {
        return response -> {
            try {
                String jsonPayload = mapper.writeValueAsString(payload);
                return response.bodyToMono(FailedTransactionResponse.class)
                        .flatMap(ftr -> {
                            if(ftr.getAppName().equals("Client"))
                                ftr.setAppName(appName);
                            ftr.setFailedPayload(Optional.of(ftr)
                                    .map(FailedTransactionResponse::getFailedPayload)
                                    .orElse(jsonPayload));
                            ftr.setOriginalStatusCode(Optional.of(ftr)
                                    .map(FailedTransactionResponse::getOriginalStatusCode)
                                    .orElse(response.statusCode()));
                            return Mono.error(new FailedTransactionException(ftr));
                        });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Function<Throwable, Throwable> handleCommunicationError(String appName, String url, Object payload) {
        return throwable -> {
            try{
                String jsonPayload = mapper.writeValueAsString(payload);
                return new FailedTransactionException(appName, url, jsonPayload, throwable, HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
