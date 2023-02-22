package com.rezz.orchestrationservice.controller;


import com.rezz.model.account.*;
import com.rezz.model.audit.CreateAuditEvent;
import com.rezz.model.audit.UpdateAuditEvent;
import com.rezz.model.audit.UpdateAuditEventNewUser;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.CreateUserResponse;
import com.rezz.model.user.User;
import com.rezz.orchestrationservice.client.AccountClient;
import com.rezz.orchestrationservice.client.AuditClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/user/old")
@RequiredArgsConstructor
@Slf4j
public class OldUserController {

    private final ConversionService converter;
    private final AccountClient accountClient;
    private final AuditClient auditClient;


    @Deprecated
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreateUserResponse> createUser(@RequestBody GenerateUserRequest request) {

        log.info("Recieved request to create user: {}", request);
        CreateAuditEvent auditEvent = converter.convert(request, CreateAuditEvent.class);

        return Mono.justOrEmpty(auditEvent)
                .flatMap(auditClient::createRecord)
                .flatMap(event ->
                        accountClient.createUser(request)
                                .publishOn(Schedulers.boundedElastic())
                                .doOnNext(createUserResponse -> {
                                    log.info("Got a response back from the account service: {}, with uuid: {}", createUserResponse, event.getTransactionUuid());
                                    auditClient.updateTransaction(UpdateAuditEventNewUser.builder()
                                            .accountId(createUserResponse.getAccountId())
                                            .userId(createUserResponse.getUserId())
                                            .transactionId(event.getTransactionUuid())
                                            .status("SUCCESS")
                                            .build()).subscribe();
                                })
                                .onErrorResume(throwable -> {
                                    log.error("Encountered Error: {}", throwable);
                                    return auditClient.updateTransaction(UpdateAuditEvent.builder()
                                                    .transactionId(event.getTransactionUuid())
                                                    .status("ERROR")
                                                    .build())
                                            .then(Mono.error(new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT)));
                                }));

    }
}
