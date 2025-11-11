package com.travel.gateway;

import com.travel.gateway.security.KeycloakTokenRelayFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter; // <-- Importante
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    // Este método devuelve la interfaz GatewayFilter (que KeycloakTokenRelayFilter implementa)
    @Bean
    public GatewayFilter keycloakTokenRelayFilter() {
        return new KeycloakTokenRelayFilter(); // Crea una instancia de la clase del filtro
    }
}