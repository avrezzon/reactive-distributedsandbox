package com.rezz.userservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services.uri")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUriConfig {
    private String loan;
}
