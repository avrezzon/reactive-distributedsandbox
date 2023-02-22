package com.rezz.orchestrationservice.client;

import com.rezz.model.audit.AuditRecord;
import com.rezz.model.audit.CreateAuditEvent;
import com.rezz.model.audit.UpdateAuditEvent;
import com.rezz.model.audit.UpdateAuditEventNewUser;
import com.rezz.orchestrationservice.config.ServiceUriConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditClient {

    private final WebClient webClient;
    private final ServiceUriConfig uris;

    public Mono<AuditRecord> createRecord(CreateAuditEvent event){
        return webClient.post()
                .uri(uris.getAudit() + "/audit")
                .bodyValue(event)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    if (response.statusCode().is4xxClientError()) {
                        log.error("Response from service is 4xx");
                    } else {
                        log.error("Response from service is 5xx");
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .bodyToMono(AuditRecord.class);

    }

    public Mono<Void> updateTransaction(UpdateAuditEvent event){
        log.info("Updating transaction: {}", event);
        return webClient.put()
                .uri(uris.getAudit() + "/audit")
                .bodyValue(event)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    if (response.statusCode().is4xxClientError()) {
                        log.error("Response from service is 4xx");
                    } else {
                        log.error("Response from service is 5xx");
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .bodyToMono(Void.class);

    }

    public Mono<Void> updateTransaction(UpdateAuditEventNewUser event){
        log.info("Updating transaction: {}", event);
        return webClient.put()
                .uri(uris.getAudit() + "/audit/userRegistration")
                .bodyValue(event)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> {
                    if (response.statusCode().is4xxClientError()) {
                        log.error("Response from service is 4xx");
                    } else {
                        log.error("Response from service is 5xx");
                    }

                    return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
                })
                .bodyToMono(Void.class);

    }

}
