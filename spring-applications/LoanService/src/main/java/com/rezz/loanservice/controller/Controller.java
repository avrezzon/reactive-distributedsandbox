package com.rezz.loanservice.controller;

import com.rezz.model.loan.LoanApplicationRequest;
import com.rezz.model.loan.LoanApplicationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/loans")
@Slf4j
public class Controller {

    private boolean errorToggle = false;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<LoanApplicationResponse> processLoanApplication(@RequestBody LoanApplicationRequest request) {
        log.info("Processing loan application with: {}", request);

        shouldCauseProblems();

        if(Objects.isNull(request.getSsn()))
            throw new IllegalArgumentException("Social Security Number is required for loan processing");

        return Mono.just(LoanApplicationResponse.builder()
                .interestRate(calculateInterestRate(request.getCreditRating()))
                .loanAmount(request.getLoanAmount())
                .loanTerm(request.getLoanTerm())
                .build());

    }

    @GetMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public boolean toggleError() {
        this.errorToggle = !errorToggle;
        return this.errorToggle;
    }

    private void shouldCauseProblems() {
        if (errorToggle)
            throw new RuntimeException("Man this sucks, something went wrong maybe try again later");
    }

    private Double calculateInterestRate(Integer creditScore) {
        return Math.random() * ((creditScore >= 800) ? 10 : 100);
    }

}
