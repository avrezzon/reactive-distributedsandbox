package com.rezz.orchestrationservice.controller;

import com.rezz.model.account.Account;
import com.rezz.model.account.GenerateAccountRequest;
import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.LoanApplication;
import com.rezz.model.user.User;
import com.rezz.orchestrationservice.client.AccountCommunication;
import com.rezz.orchestrationservice.client.UserCommunication;
import com.rezz.orchestrationservice.model.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserCommunication userCommunication;
    private final AccountCommunication accountCommunication;

    /**
     * This endpoint highlights how the orchestration service handles 4xx and 5xx from a direct service call
     * ex: A -> B
     *
     * To simulate a 5xx error, hit either UserService or AccountService /error to trigger a runtime exception
     * Reference diagram: Scenario 1
     *
     * Review the requirements within the UserService and the Account service for how to trigger a 4xx error
     * Reference diagram: Scenario 2
     * */
    @GetMapping
    public Mono<User> findUserDetails(@RequestParam(required = false) UUID id,
                                      @RequestParam(required = false) String ssn) {

        Mono<User> userMono;

        if (Objects.nonNull(id)) {
            log.info("looking up user by UUID");
            userMono = getAccountDetails(id);
        } else if (Objects.nonNull(ssn)) {
            log.info("Looking up user by ssn");
            userMono = userCommunication.findUserBySsn(ssn)
                    .flatMap(user ->
                            accountCommunication.findAccountsForUser(user.getId())
                                    .flatMap(accounts -> {
                                        user.setAccounts(accounts);
                                        return Mono.just(user);
                                    }));

        } else
            throw new IllegalArgumentException("Either user id or ssn needs to be present to look up user");

        return userMono;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createNewUser(@RequestBody CreateUserRequest request) {
        log.info("Creating a new user with details: {}", request);

        return Mono.just(request)
                .map(this::userRequest)
                .flatMap(userCommunication::createUser)
                .flatMap(user ->
                        Mono.just(accountRequest(user, request))
                                .flatMap(accountCommunication::createNewAccount)
                                .flatMap(account -> {
                                    user.setAccounts(List.of(account));
                                    return Mono.just(user);
                                }));
    }

    @PostMapping("/new-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> createNewAccountForUser(@RequestBody GenerateAccountRequest request) {
        return accountCommunication.createNewAccount(request)
                .then(getAccountDetails(request.getOwnerId()));
    }

    /**
     * This endpoint is to showcase how the error handler manages situations where there are multiple calls
     * ex: A -> B -> C'
     *
     * Service C is where the error could occur, and ensures that Service A doesnt report Service B as failing
     * Note: to try this out, make sure to hit the /error endpoint in LoanService to watch this scenario
     * Reference diagram: Scenario 3
     *
     * Note: to see how User Service handles a 4xx response from Loan Service, enable the /error endpoint on UserService
     * Reference diagram: Scenario 4
     * */
    @PostMapping("/request-loan")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Account> requestForLoan(@RequestParam Double loanAmount, @RequestParam Integer loanTerm, @RequestParam UUID userId){

        if(Objects.isNull(loanAmount) || Objects.isNull(loanTerm) || Objects.isNull(userId))
            throw new IllegalArgumentException("Either user id, loan term and loan amount need to be present to process loan");

        LoanApplication application = LoanApplication.builder()
                .loanAmount(loanAmount)
                .loanTerm(loanTerm)
                .ownerId(userId)
                .build();

        return Mono.just(application)
                .flatMap(userCommunication::applyForLoan)
                .flatMap(loanApplicationResponse -> Mono.just(accountRequest(userId, loanApplicationResponse))
                        .flatMap(accountCommunication::createNewAccount));

    }

    private GenerateUserRequest userRequest(CreateUserRequest request) {
        return GenerateUserRequest.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .ssn(request.getSsn())
                .build();
    }

    private GenerateAccountRequest accountRequest(User user, CreateUserRequest request) {
        return GenerateAccountRequest.builder()
                .ownerId(user.getId())
                .accountType("SAVINGS")
                .startingBalance(request.getSavingsAmount())
                .build();
    }

    private GenerateAccountRequest accountRequest(UUID userId, LoanApplicationResponse loanDocument){
        return GenerateAccountRequest.builder()
                .startingBalance(loanDocument.getLoanAmount())
                .accountType("LOAN")
                .ownerId(userId)
                .build();
    }

    private Mono<User> getAccountDetails(UUID id) {
        return Mono.zip(userCommunication.findUserById(id),
                        accountCommunication.findAccountsForUser(id))
                .flatMap(results -> {
                    User userRecords = new User();
                    userRecords.setId(id);
                    userRecords.setSsn(results.getT1().getSsn());
                    userRecords.setFirstname(results.getT1().getFirstname());
                    userRecords.setLastname(results.getT1().getLastname());
                    userRecords.setAccounts(results.getT2());
                    return Mono.just(userRecords);
                });
    }

}
