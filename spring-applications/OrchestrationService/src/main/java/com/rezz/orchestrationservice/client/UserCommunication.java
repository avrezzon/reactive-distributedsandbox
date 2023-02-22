package com.rezz.orchestrationservice.client;

import com.rezz.model.loan.LoanApplicationResponse;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.LoanApplication;
import com.rezz.model.user.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserCommunication {

    Mono<User> findUserById(UUID id);
    Mono<User> findUserBySsn(String ssn);
    Mono<User> createUser(GenerateUserRequest request);
    Mono<LoanApplicationResponse> applyForLoan(LoanApplication application);
    Mono<Void> deleteUser(UUID id);

}
