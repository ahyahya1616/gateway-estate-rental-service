package ma.fstt.gatewayservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Gestionnaire de fallback pour le Gateway.
 * Retourne des réponses HTTP propres (503) au lieu de 500 brut.
 * NE PROVOQUE PAS de logout côté frontend.
 */
@Component
public class GatewayFallbackHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayFallbackHandler.class);

    /**
     * Fallback générique pour tous les microservices.
     * Retourne 503 Service Unavailable avec un message clair.
     */
    public Mono<ServerResponse> handleFallback(ServerRequest request) {
        String path = request.path();
        String serviceName = extractServiceName(path);

        log.warn("Circuit breaker fallback triggered for service: {} - Path: {}", serviceName, path);

        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Service Unavailable",
                "message", serviceName + " is temporarily unavailable. Please try again later.",
                "path", path
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    /**
     * Fallback spécifique pour notification-microservice.
     */
    public Mono<ServerResponse> handleNotificationFallback(ServerRequest request) {
        log.warn("Notification service fallback triggered - Path: {}", request.path());

        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Notification Service Unavailable",
                "message", "Notifications are temporarily unavailable. Your request has been logged.",
                "path", request.path()
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    /**
     * Fallback spécifique pour rental-agreement-microservice.
     */
    public Mono<ServerResponse> handleRentalAgreementFallback(ServerRequest request) {
        log.warn("Rental Agreement service fallback triggered - Path: {}", request.path());

        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Rental Agreement Service Unavailable",
                "message", "Rental agreement service is temporarily unavailable. Please try again later.",
                "path", request.path()
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    /**
     * Fallback spécifique pour property-microservice.
     */
    public Mono<ServerResponse> handlePropertyFallback(ServerRequest request) {
        log.warn("Property service fallback triggered - Path: {}", request.path());

        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Property Service Unavailable",
                "message", "Property service is temporarily unavailable. Please try again later.",
                "path", request.path()
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    /**
     * Fallback spécifique pour user-management-service.
     */
    public Mono<ServerResponse> handleUserFallback(ServerRequest request) {
        log.warn("User service fallback triggered - Path: {}", request.path());

        Map<String, Object> errorResponse = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "User Service Unavailable",
                "message", "User management service is temporarily unavailable. Please try again later.",
                "path", request.path()
        );

        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }



    /**
     * Extrait le nom du service depuis le path pour logging.
     */
    private String extractServiceName(String path) {
        if (path.contains("/notifications")) return "Notification Service";
        if (path.contains("/rentalAgreement")) return "Rental Agreement Service";
        if (path.contains("/property")) return "Property Service";
        if (path.contains("/users")) return "User Service";
        if (path.contains("/auth")) return "Authorization Service";
        return "Unknown Service";
    }
}