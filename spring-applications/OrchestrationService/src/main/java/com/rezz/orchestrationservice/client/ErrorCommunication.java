package com.rezz.orchestrationservice.client;


import com.rezz.restcommunications.model.FailedTransactionResponse;
import reactor.core.publisher.Mono;

public interface ErrorCommunication {

    Mono<Void> createNewEvent(FailedTransactionResponse response);

}
