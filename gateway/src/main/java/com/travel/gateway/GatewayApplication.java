package com.travel.gateway;

import com.travel.gateway.security.KeycloakTokenRelayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * Define el bean del filtro custom. El nombre del bean es el nombre del método:
     * 'keycloakTokenRelayFilter'.
     */
    @Bean
    public GatewayFilter keycloakTokenRelayFilter() {
        return new KeycloakTokenRelayFilter();
    }
}