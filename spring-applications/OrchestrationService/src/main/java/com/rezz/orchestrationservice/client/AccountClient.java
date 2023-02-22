package com.rezz.orchestrationservice.client;

import com.rezz.model.account.Account;
import com.rezz.model.account.GenerateAccountRequest;
import com.rezz.model.user.CreateUserResponse;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.orchestrationservice.config.ServiceUriConfig;
import com.rezz.restcommunications.exception.FailedTransactionException;
import com.rezz.restcommunications.webclient.WebClientManager;
import com.rezz.restcommunications.webclient.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.util.function.Predicate.not;

@Component
@Slf4j
public class AccountClient implements AccountCommunication {

    public static final String CALLING_SERVICE = "AccountService";
    public static final String ORIGINATING_SERVICE = "OrchestrationService";
    private final WebClient webClient;
    private final ServiceUriConfig uris;

    private final WebClientManager webClientManager;

    public AccountClient(WebClient webClient, ServiceUriConfig uris){
        this.webClient = webClient;
        this.uris = uris;
        this.webClientManager = new WebClientManager("AccountService", "OrchestrationService");
    }

    @Deprecated
    public Mono<CreateUserResponse> createUser(GenerateUserRequest request) {
        log.info("CREATING THE USER: {}", request);

        return webClient.post()
                .uri(uris.getAccount() + "/user")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    log.error("Encountered issue from AccountService: {}", response);
                    if (response.statusCode().is4xxClientError()) {
                        log.error("Response from service is 4xx");
                    } else {
                        log.error("Response from service is 5xx");
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .bodyToMono(CreateUserResponse.class);
    }

    @Override
    public Mono<List<Account>> findAccountsForUser(UUID userId) {

        String url = String.format("%s/account/%s", uris.getAccount(), userId);
        log.info(url);

        return webClient.get()
                .uri(URI.create(url))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, WebClientUtils.handle4xxClientError(userId))
                .onStatus(HttpStatus::is5xxServerError, WebClientUtils.handle5xxServerError(CALLING_SERVICE, url, userId))
                .bodyToFlux(Account.class)
                .collectList()
                .onErrorMap(not(FailedTransactionException.class::isInstance),
                        WebClientUtils.handleCommunicationError(ORIGINATING_SERVICE, url, userId));
    }

    @Override
    public Mono<Account> createNewAccount(GenerateAccountRequest request) {

        String url = String.format("%s/account", uris.getAccount());
        log.info(url);

        //NOTE that this is just to show where that stuff can be abstracted, however this doesn't apply all the time, see above
        return webClientManager.postRequest(url, request, new ParameterizedTypeReference<Account>() {});

    }
}
