package com.rezz.userservice.client;

import com.rezz.model.loan.LoanApplicationRequest;
import com.rezz.model.loan.LoanApplicationResponse;
import reactor.core.publisher.Mono;

public interface LoanServiceCommunication {

    Mono<LoanApplicationResponse> submitLoanApplicationRequest(LoanApplicationRequest request);
}
