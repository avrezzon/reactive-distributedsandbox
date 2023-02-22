package com.rezz.orchestrationservice.client;

import com.rezz.model.account.Account;
import com.rezz.model.account.GenerateAccountRequest;
import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.user.CreateUserResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface AccountCommunication {

    @Deprecated
    Mono<CreateUserResponse> createUser(GenerateUserRequest request);
    Mono<List<Account>> findAccountsForUser(UUID userId);

    Mono<Account> createNewAccount(GenerateAccountRequest request);
}
