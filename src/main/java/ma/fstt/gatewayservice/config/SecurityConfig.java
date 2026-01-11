package ma.fstt.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(exchanges -> exchanges
                        // ========================================
                        //  OPTIONS : TOUJOURS PUBLIC (CORS preflight)
                        // ========================================
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ========================================
                        //  ROUTES AUTH PUBLIQUES (sans token)
                        // ========================================
                        .pathMatchers(HttpMethod.GET, "/api/auth/metamask/nonce").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/metamask/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/metamask/refresh").permitAll()


                        // Notification
                        .pathMatchers("/ws-notifications/**").permitAll()
                        // OAuth2 endpoints publics
                        .pathMatchers("/oauth2/**", "/.well-known/**").permitAll()

                        // ========================================
                        // ROUTES PROPERTY MICROSERVICE : GET PUBLIC
                        // ========================================
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/properties/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/rooms/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/properties/room-images/**").permitAll()

                        //  Tous les autres  (POST, PUT, DELETE, PATCH) sur property-microservice nécessitent JWT
                        .pathMatchers("/api/property-microservice/**").authenticated()

                        // ========================================
                        // ROUTES USER SERVICE
                        // ========================================


                        // GET /api/users/wallet/{wallet} : PUBLIC
                        .pathMatchers(HttpMethod.GET, "/api/users/wallet/**").permitAll()

                        // POST /api/users : PUBLIC (inscription)
                        .pathMatchers(HttpMethod.POST, "/api/users").permitAll()

                        //  Toutes les autres routes /api/users/** nécessitent JWT
                        .pathMatchers("/api/users/**").authenticated()

                        .pathMatchers(HttpMethod.GET, "/api/users").authenticated()
                        //  ACTUATOR (monitoring)
                        .pathMatchers("/actuator/health", "/actuator/info" , "/actuator/env").permitAll()

                        //  RENTAL AGREEMENT : JWT OBLIGATOIRE
                        .pathMatchers("/api/rentalAgreement-microservice/**").authenticated()

                        // ========================================
                        // RÈGLE PAR DÉFAUT : JWT OBLIGATOIRE
                        // ========================================
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}));

        return http.build();
    }

}