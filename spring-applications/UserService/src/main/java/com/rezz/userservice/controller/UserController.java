package com.rezz.userservice.controller;

import com.rezz.exception.ResourceNotFoundException;
import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.LoanApplication;
import com.rezz.userservice.model.User;
import com.rezz.userservice.repository.UserRepository;
import com.rezz.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private boolean errorToggle = false;
    private final UserRepository repository;
    private final UserService service;

    @GetMapping
    public Flux<User> findAllRegisteredUsers(){

        shouldCauseProblems();

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<User> findUserById(@PathVariable UUID id){

        log.info("Looking up user: {}", id);

        shouldCauseProblems();

        return repository.findById(id)
                .doOnNext(user -> log.info("Found user: {}", user))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User doesn't exist", id)));
    }

    @GetMapping("/lookup/{ssn}")
    public Mono<User> findUserBySsn(@PathVariable String ssn) {

        log.info("Looking up user with ssn: {}", ssn);

        shouldCauseProblems();

        return repository.findBySsn(ssn)
                .doOnNext(user -> log.info("Found user: {}", user))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User doesn't exist", ssn)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createUser(@RequestBody GenerateUserRequest request){
        log.info("Creating user and account with: {}", request);

        shouldCauseProblems();

        User user = new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setSsn(request.getSsn());

        return Mono.just(user.getSsn())
                .flatMap(repository::findBySsn)
                .defaultIfEmpty(User.NO_OP)
                .flatMap(existingUser -> {
                    if (existingUser.equals(User.NO_OP))
                        return repository.save(user);
                    else {
                        return Mono.error(new IllegalArgumentException("User already exists."));
                    }
                })
                .doOnNext(user1 -> log.info("Created user: {}", user1));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable UUID id){
        return Mono.just(id)
                .flatMap(repository::findById)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User doesn't exist", id)))
                .flatMap(repository::delete);
    }

    @PostMapping("/loan-application")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LoanApplicationResponse> applyForNewLoan(@RequestBody LoanApplication request) {
        log.info("Received request for loan application: {}", request);

        return service.applyForLoan(request, errorToggle);
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
