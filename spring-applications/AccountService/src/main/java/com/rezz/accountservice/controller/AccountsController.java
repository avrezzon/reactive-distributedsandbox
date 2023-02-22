package com.rezz.accountservice.controller;

import com.rezz.accountservice.entity.Account;
import com.rezz.accountservice.service.AccountService;
import com.rezz.model.account.GenerateAccountRequest;
import com.rezz.model.account.TransactionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountsController {

    //TODO need to create the error handler

    private boolean errorToggle = false;
    private final AccountService accountService;

    @GetMapping
    public Flux<Account> findAllAccounts(){
        shouldCauseProblems();
        return accountService.getAllAccounts();
    }

    @GetMapping("/{userId}")
    public Flux<Account> getUserAccounts(@PathVariable UUID userId) {
        log.info("Looking for accounts for user: {}", userId);
        shouldCauseProblems();
        return accountService.getAllAccountsByOwnerId(userId)
                .doOnNext(account -> log.info("Found account for user: {}, account: {}", userId, account));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> createAccount(@RequestBody GenerateAccountRequest request){
        log.info("Obtained a request to create account for: {}", request);
        shouldCauseProblems();
        return accountService.createAccount(request)
                .doOnNext(account -> log.info("Created account: {}", account));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable int id){
        log.info("deleting account: {}",id);
        shouldCauseProblems();
        return accountService.deleteAccount(id);
    }

    @GetMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public boolean toggleError(){
        this.errorToggle = !errorToggle;
        return this.errorToggle;
    }

    private void shouldCauseProblems(){
        if(errorToggle)
            throw new RuntimeException("Man this sucks, something went wrong maybe try again later");
    }

}
