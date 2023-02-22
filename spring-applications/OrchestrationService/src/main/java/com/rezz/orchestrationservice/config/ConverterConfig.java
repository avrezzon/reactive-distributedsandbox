package com.rezz.orchestrationservice.config;

import com.rezz.orchestrationservice.converter.CreateUserRequestToAuditEventConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConverterConfig implements WebMvcConfigurer {

    private final CreateUserRequestToAuditEventConverter createUserRequestToAuditEventConverter;

    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addConverter(createUserRequestToAuditEventConverter);
    }
}
