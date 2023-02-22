package com.rezz.restcommunications.webclient;

import com.rezz.restcommunications.exception.FailedTransactionException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.util.function.Predicate.not;

public class WebClientManager {
    private final WebClient webClient;
    private final String callingService;
    private final String originatingService;

    public WebClientManager(String callingService, String originatingService){
        this.callingService = callingService;
        this.originatingService = originatingService;
        this.webClient = WebClient.create();
    }

    public <T, K> Mono<T> postRequest(String url, K body, ParameterizedTypeReference<T> typeReference) {
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(body))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(callingService, url, body))
                .bodyToMono(typeReference)
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(originatingService, url, body));
    }
}
