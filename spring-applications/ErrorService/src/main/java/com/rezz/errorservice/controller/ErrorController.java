package com.rezz.errorservice.controller;

import com.rezz.errorservice.converter.ErrorEventConverter;
import com.rezz.errorservice.model.ErrorEvent;
import com.rezz.errorservice.repository.ErrorRepository;
import com.rezz.restcommunications.model.FailedTransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("error")
@RequiredArgsConstructor
@Slf4j
public class ErrorController {
    private final ErrorEventConverter converter;
    private final ErrorRepository repository;

    @GetMapping
    public Flux<ErrorEvent> getAllEvents() {
        return repository.findAll();
    }

    @GetMapping("/app")
    public Flux<ErrorEvent> findErrorsByApp(@RequestParam String appName){
        return repository.findAllByAppName(appName);
    }

    @GetMapping("/payload")
    public Flux<ErrorEvent> findErrorsByPayloadContents(@RequestParam String search){
        return repository.findAllByPayloadContainsIgnoreCase(search);
    }

    @GetMapping("/timerange")
    public Flux<ErrorEvent> findByTimeWindow(@RequestParam LocalDateTime start,
                                             @RequestParam LocalDateTime end){
        return repository.findAllByEventTimestampBetween(start, end);
    }

    //In this case the orchestration service isn't going to do anything with the response, thus return void
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createNewRecord(@RequestBody FailedTransactionResponse response) {
        log.info("Entering a new FailedTransactionResponse to the database, {}", response);
        return Mono.just(response)
                .map(converter::createEvent)
                .flatMap(repository::save)
                .doOnNext(o -> log.info("Created new record: {}", o))
                .then();
    }
}
