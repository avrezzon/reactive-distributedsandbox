package com.rezz.userservice.service;

import com.rezz.exception.ResourceNotFoundException;
import com.rezz.model.loan.LoanApplicationRequest;
import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.model.user.LoanApplication;
import com.rezz.userservice.client.LoanServiceCommunication;
import com.rezz.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final LoanServiceCommunication loanServiceCommunication;


    public Mono<LoanApplicationResponse> applyForLoan(LoanApplication application, boolean errorToggle) {

        return repository.findById(application.getOwnerId())
                .doOnNext(user -> log.info("Obtained user: {}", user))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User doesn't exist", application.getOwnerId())))
                .flatMap(user ->
                        Mono.just(LoanApplicationRequest.builder()
                                .creditRating(determineCreditScore(user.getId()))
                                .loanAmount(application.getLoanAmount())
                                .loanTerm(application.getLoanTerm())
                                .ssn((errorToggle) ? null : user.getSsn())
                                .build()))
                .doOnNext(request -> log.info("Created the loan application: {}", request))
                .flatMap(loanServiceCommunication::submitLoanApplicationRequest)
                .doOnSuccess(loanApplicationResponse -> log.info("Successfully applied for the loan, {}", loanApplicationResponse));
    }

    private Integer determineCreditScore(UUID userId) {
        //... other logic in here but lets say that every applicant has amazing credit
        return Integer.valueOf(800);
    }

}
