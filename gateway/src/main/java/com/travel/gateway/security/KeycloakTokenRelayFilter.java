package com.travel.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

// IMPLEMENTACIÓN DIRECTA: La clase implementa GatewayFilter y anula el método filter.
public class KeycloakTokenRelayFilter implements GatewayFilter {

    /**
     * Lógica principal del filtro custom.
     * Extrae el ID de usuario y Roles del JWT y los inyecta en los headers.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    if (auth.getPrincipal() instanceof Jwt jwt) {

                        // 1. Obtener el Subject (ID del usuario)
                        String subject = jwt.getSubject();

                        // 2. Obtener los Roles
                        List<String> roles = auth.getAuthorities().stream()
                                .map(a -> ((SimpleGrantedAuthority) a).getAuthority())
                                .collect(Collectors.toList());

                        // 3. Mutar (modificar) la solicitud original
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(originalRequest -> originalRequest
                                        // Pasar el ID del usuario
                                        .header("X-User-ID", subject)
                                        // Pasar los roles (ej: ROLE_DRIVER, ROLE_PASSENGER)
                                        .header("X-User-Roles", String.join(",", roles))
                                        // Opcional: Pasar el token original (Token Relay)
                                        .header("Authorization", "Bearer " + jwt.getTokenValue())
                                        .build())
                                .build();

                        return chain.filter(mutatedExchange);
                    }
                    // Si la autenticación no es un JWT, simplemente continúa
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // Continuar si no hay usuario
    }
}