package com.rezz.accountservice.repository;

import com.rezz.accountservice.entity.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface AccountRepository extends ReactiveCrudRepository<Account, Integer> {
    Flux<Account> findAllByOwnerId(UUID ownerId);
}
