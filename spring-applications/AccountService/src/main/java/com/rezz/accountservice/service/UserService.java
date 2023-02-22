package com.rezz.accountservice.service;

import com.rezz.accountservice.entity.User;
import com.rezz.accountservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public Flux<User> findAllUsers() {
        return repo.findAll();
    }

    public Mono<User> findUserById(int id) {
        return repo.findById(id)
                .doOnNext(user -> log.debug("Found user with id {}", user.getId()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    public Mono<User> createUser(User user) {
        return Mono.just(user.getSsn())
                .flatMap(repo::findBySsn)
                .defaultIfEmpty(User.NO_OP)
                .flatMap(existingUser -> {
                    if (existingUser.equals(User.NO_OP))
                        return repo.save(user);
                    else {
                        return Mono.error(new IllegalArgumentException("User already exists."));
                    }
                });
    }
}
