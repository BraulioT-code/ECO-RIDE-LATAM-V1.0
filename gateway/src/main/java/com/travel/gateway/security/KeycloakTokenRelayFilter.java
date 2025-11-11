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

// ¡Clase simple que IMPLEMENTA la interfaz!
public class KeycloakTokenRelayFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    if (auth.getPrincipal() instanceof Jwt jwt) {

                        String subject = jwt.getSubject();
                        List<String> roles = auth.getAuthorities().stream()
                                .map(a -> ((SimpleGrantedAuthority) a).getAuthority())
                                .collect(Collectors.toList());

                        // Mutar (modificar) la solicitud original
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(originalRequest -> originalRequest
                                        .header("X-User-ID", subject)
                                        .header("X-User-Roles", String.join(",", roles))
                                        .header("Authorization", "Bearer " + jwt.getTokenValue())
                                        .build())
                                .build();

                        return chain.filter(mutatedExchange);
                    }
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}