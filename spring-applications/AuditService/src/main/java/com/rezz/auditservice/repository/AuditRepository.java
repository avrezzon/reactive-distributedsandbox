package com.rezz.auditservice.repository;

import com.rezz.auditservice.model.AuditRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface AuditRepository extends ReactiveCrudRepository <AuditRecord, UUID> {
    Flux<AuditRecord> findAllByUserId (int userId);
}
