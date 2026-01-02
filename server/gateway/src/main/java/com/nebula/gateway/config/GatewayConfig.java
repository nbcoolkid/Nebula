package com.nebula.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Gateway Configuration
 * Configure web clients and other gateway-related beans
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    /**
     * WebClient builder for downstream service calls
     * Future: Use for custom filters or load balancing
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
