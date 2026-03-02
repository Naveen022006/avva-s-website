package com.avvahomefoods.config;

import org.springframework.context.annotation.Configuration;

// This file is intentionally left empty.
// CORS is configured centrally in SecurityConfig.java via corsConfigurationSource().
// Having two separate CORS configurations causes request failures in Spring Security.
@Configuration
public class WebConfig {
    // CORS is handled by SecurityConfig
}
