package com.rezz.accountservice.service;

import com.rezz.accountservice.entity.Account;
import com.rezz.accountservice.repository.AccountRepository;
import com.rezz.model.account.GenerateAccountRequest;
import com.rezz.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository repo;

    public Flux<Account> getAllAccounts() {
        return repo.findAll();
    }

    public Flux<Account> getAllAccountsByOwnerId(UUID ownerId) {
        return repo.findAllByOwnerId(ownerId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cannot find accounts with that owner id",ownerId)));
    }

    public Mono<Account> findAccountById(int id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cannot find accounts with that id", id)));

    }

    public Mono<Void> deleteAccount(int id){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Cannot find accounts with that id", id)))
                .flatMap(repo::delete);
    }

    public Mono<Account> createAccount(GenerateAccountRequest request) {
        log.info("Creating account with info: {}", request);
        return Mono.just(request)
                .map(req -> {
                    Account account = new Account();
                    account.setOwnerId(req.getOwnerId());
                    account.setAccountType(req.getAccountType());
                    account.setBalance(req.getStartingBalance());
                    return account;
                })
                .flatMap(repo::save);
    }
}
