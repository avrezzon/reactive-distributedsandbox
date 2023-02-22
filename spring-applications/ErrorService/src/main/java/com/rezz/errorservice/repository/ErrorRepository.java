package com.rezz.errorservice.repository;

import com.rezz.errorservice.model.ErrorEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ErrorRepository extends ReactiveCrudRepository<ErrorEvent, UUID> {
    Flux<ErrorEvent> findAllByEventTimestampBetween(LocalDateTime start, LocalDateTime end);
    Flux<ErrorEvent> findAllByAppName(String appName);
    Flux<ErrorEvent> findAllByPayloadContainsIgnoreCase(String searchContent);
}
