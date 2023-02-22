package com.rezz.orchestrationservice.converter;

import com.rezz.model.user.GenerateUserRequest;
import com.rezz.model.audit.CreateAuditEvent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateUserRequestToAuditEventConverter implements Converter<GenerateUserRequest, CreateAuditEvent> {


    @Override
    public CreateAuditEvent convert(GenerateUserRequest source) {
        return CreateAuditEvent.builder()
                .userId(0)
                .accountId(0)
                .operation("CREATE USER")
                .build();
    }
}
