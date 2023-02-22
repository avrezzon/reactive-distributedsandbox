package com.rezz.userservice.client;

import com.rezz.model.loan.LoanApplicationRequest;
import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.webclient.WebClientUtils;
import com.rezz.userservice.config.ServiceUriConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanServiceClient implements LoanServiceCommunication{

    private final WebClient webClient;
    private final ServiceUriConfig uris;

    @Override
    public Mono<LoanApplicationResponse> submitLoanApplicationRequest(LoanApplicationRequest request) {

        String url = String.format("%s/loans", uris.getLoan());
        log.info(url);

        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError("UserService",request))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError("LoanService", url, request))
                .bodyToMono(LoanApplicationResponse.class)
                .onErrorMap(not(FailedTransactionException.class::isInstance), WebClientUtils.handleCommunicationError("UserService", url, request));
    }
}
