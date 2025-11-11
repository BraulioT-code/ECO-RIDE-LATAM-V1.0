package com.travel.gateway;

import com.travel.gateway.security.KeycloakTokenRelayFilter; // <-- ¡Asegúrate de este import!
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    // El bean debe tener el nombre en camelCase: keycloakTokenRelayFilter
    @Bean
    public GatewayFilter keycloakTokenRelayFilter() {
        return new KeycloakTokenRelayFilter();
    }
}