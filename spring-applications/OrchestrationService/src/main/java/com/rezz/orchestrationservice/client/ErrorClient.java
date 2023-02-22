package com.rezz.orchestrationservice.client;

import com.rezz.orchestrationservice.config.ServiceUriConfig;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class ErrorClient implements ErrorCommunication{

    public static final String CALLING_SERVICE = "ErrorService";
    public static final String ORIGINATING_SERVICE = "OrchestrationService";
    private final WebClient webClient;
    private final ServiceUriConfig uris;
    @Override
    public Mono<Void> createNewEvent(FailedTransactionResponse response) {

        String url = String.format("%s/error", uris.getError());
        log.info(url);

        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(response)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(throwable -> log.error("Something went wrong with the ErrorService but we aren't blowing up here"))
                .onErrorResume(throwable -> Mono.empty());
    }
}
