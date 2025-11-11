package com.travel.gateway.config;

import com.travel.gateway.security.KeycloakTokenRelayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    private final KeycloakTokenRelayFilter keycloakTokenRelayFilter = new KeycloakTokenRelayFilter();

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("trip-service", r -> r.path("/api/trips/**")
                        .filters(f -> f
                                .filter(keycloakTokenRelayFilter)
                                .rewritePath("/api/(?<segment>.*)", "/${segment}"))
                        .uri("lb://trip-service"))
                .route("passenger-service", r -> r.path("/api/passenger/**")
                        .filters(f -> f
                                .filter(keycloakTokenRelayFilter)
                                .rewritePath("/api/passenger/(?<segment>.*)", "/${segment}"))
                        .uri("lb://PASSENGER-SERVICE"))
                .build();
    }
}
