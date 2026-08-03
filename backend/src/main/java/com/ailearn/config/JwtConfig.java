package com.ailearn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "ailearn.jwt")
public class JwtConfig {

    private String secret;
    private Long expiration;
}
