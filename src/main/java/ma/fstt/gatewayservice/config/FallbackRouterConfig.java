package ma.fstt.gatewayservice.config;

import ma.fstt.gatewayservice.handler.GatewayFallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Configuration des routes de fallback pour le Gateway.
 * Ces routes sont appelées automatiquement par Resilience4j lorsque le circuit breaker s'ouvre.
 */
@Configuration
public class FallbackRouterConfig {

    private static final Logger log = LoggerFactory.getLogger(FallbackRouterConfig.class);

    @Bean
    public RouterFunction<ServerResponse> fallbackRoutes(GatewayFallbackHandler fallbackHandler) {
        return route(path("/fallback/notification/**"), fallbackHandler::handleNotificationFallback)
                .andRoute(path("/fallback/rental-agreement/**"), fallbackHandler::handleRentalAgreementFallback)
                .andRoute(path("/fallback/property/**"), fallbackHandler::handlePropertyFallback)
                .andRoute(path("/fallback/user/**"), fallbackHandler::handleUserFallback);
     }
}