package com.rezz.orchestrationservice.client;

import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.LoanApplication;
import com.rezz.model.user.User;
import com.rezz.orchestrationservice.config.ServiceUriConfig;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.webclient.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import static java.util.function.Predicate.not;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserClient implements UserCommunication {

    public static final String ORIGINATING_SERVICE = "OrchestrationService";
    public static final String CALLING_SERVICE = "UserService";
    private final ServiceUriConfig uriConfig;
    private final WebClient webClient;

    @Override
    public Mono<User> findUserById(UUID id) {
        String url = String.format("%s/user/%s", uriConfig.getUser(), id);
        log.info(url);
        return webClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(id))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(CALLING_SERVICE, url, id))
                .bodyToMono(User.class)
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(ORIGINATING_SERVICE, url, id));

    }

    @Override
    public Mono<User> findUserBySsn(String ssn) {

        String url = String.format("%s/user/lookup/%s", uriConfig.getUser(), ssn);
        log.info(url);
        return webClient
                .get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(ssn))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(CALLING_SERVICE, url, ssn))
                .bodyToMono(User.class)
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(ORIGINATING_SERVICE, url, ssn));
    }

    @Override
    public Mono<User> createUser(GenerateUserRequest request) {
        String url = String.format("%s/user", uriConfig.getUser());
        log.info(url);
        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(request))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(CALLING_SERVICE, url, request))
                .bodyToMono(User.class)
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(ORIGINATING_SERVICE, url, request));
    }

    @Override
    public Mono<LoanApplicationResponse> applyForLoan(LoanApplication application) {
        String url = String.format("%s/user/loan-application", uriConfig.getUser());
        log.info(url);

        return webClient.post()
                .uri(URI.create(url))
                .bodyValue(application)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(application))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(CALLING_SERVICE, url, application))
                .bodyToMono(LoanApplicationResponse.class)
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(ORIGINATING_SERVICE, url, application));
    }

    @Override
    public Mono<Void> deleteUser(UUID id) {
        return null;
    }
}
