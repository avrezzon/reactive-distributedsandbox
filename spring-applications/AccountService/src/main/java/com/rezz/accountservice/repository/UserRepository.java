package com.rezz.accountservice.repository;

import com.rezz.accountservice.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    Mono<User> findBySsn(String ssn);
}
