package com.travel.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityGatewayFilterChain(ServerHttpSecurity http) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 1. Autorización: Rutas que requieren autenticación
                .authorizeExchange(exchanges -> exchanges
                        // Permitir acceso sin token a Eureka y Actuator (para métricas/salud)
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        // Proteger todas las demás rutas
                        .anyExchange().authenticated()
                )
                // 2. Resource Server: Habilita el manejo de tokens JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Usa la configuración de 'issuer-uri' de application.properties
                );

        return http.build();
    }
}