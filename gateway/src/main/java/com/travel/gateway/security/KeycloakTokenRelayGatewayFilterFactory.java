package com.travel.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeycloakTokenRelayGatewayFilterFactory
        extends AbstractGatewayFilterFactory<KeycloakTokenRelayGatewayFilterFactory.Config> {

    public KeycloakTokenRelayGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    if (auth.getPrincipal() instanceof Jwt jwt) {

                        String subject = jwt.getSubject();
                        List<String> roles = auth.getAuthorities().stream()
                                .map(a -> ((SimpleGrantedAuthority) a).getAuthority())
                                .collect(Collectors.toList());

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
                .switchIfEmpty(chain.filter(exchange))
                .then(Mono.empty());
    }

    public static class Config {
        // Puedes dejarla vacía o agregar configuraciones si lo necesitas en el futuro
    }
}
