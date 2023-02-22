package com.rezz.auditservice.controller;

import com.rezz.auditservice.model.AuditRecord;
import com.rezz.auditservice.repository.AuditRepository;
import com.rezz.model.audit.CreateAuditEvent;
import com.rezz.model.audit.UpdateAuditEvent;
import com.rezz.model.audit.UpdateAuditEventNewUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
@Slf4j
public class AuditController {

    private final AuditRepository repo;

    @GetMapping
    public Flux<AuditRecord> getAllRecords() {
        return repo.findAll();
    }

    @GetMapping("/{userId}")
    public Flux<AuditRecord> findRecordsByUserId(@PathVariable int userId) {
        return repo.findAllByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuditRecord> createNewRecord(@RequestBody CreateAuditEvent request) {

        log.info("Processing request to create new record: {}", request);

        return repo.save(AuditRecord.builder()
                        .account(request.getAccountId())
                        .operation(request.getOperation())
                        .status("STARTED")
                        .amount(request.getAmount())
                        .userId(request.getUserId())
                        .toAccount(request.getToAccount())
                        .build())
                .doOnNext(record -> log.info("Created record: {}", record));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateTransaction(@RequestBody UpdateAuditEvent request) {
        log.info("Updating event with {}", request);
        return repo.findById(request.getTransactionId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(auditRecord -> {
                    auditRecord.setStatus(request.getStatus());
                    return auditRecord;
                })
                .flatMap(repo::save)
                .doOnNext(event -> log.info("Updated the event {}", event))
                .then();
    }

    @PutMapping("/userRegistration")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateTransaction(@RequestBody UpdateAuditEventNewUser request) {
        log.info("Updating event with {}", request);
        return repo.findById(request.getTransactionId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(auditRecord -> {
                    auditRecord.setStatus(request.getStatus());
                    auditRecord.setAccount(request.getAccountId());
                    auditRecord.setUserId(request.getUserId());
                    return auditRecord;
                })
                .flatMap(repo::save)
                .doOnNext(event -> log.info("Updated the event {}", event))
                .then();
    }

}
