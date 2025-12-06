package ma.fstt.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                // Désactiver CSRF pour les API REST
                .csrf(csrf -> csrf.disable())

                // ✅ Configuration CORS explicite
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configuration des autorisations
                .authorizeExchange(exchanges -> exchanges
                        // ✅ IMPORTANT: Permettre toutes les requêtes OPTIONS (preflight)
                        .pathMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Routes publiques
                        .pathMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/.well-known/**",
                                "/actuator/health",
                                "/actuator/info",
                                "/actuator/env",
                                "/api/users"
                        ).permitAll()

                        // Toutes les autres routes nécessitent une authentification JWT
                        .anyExchange().authenticated()
                )

                // Configuration OAuth2 Resource Server (JWT)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {})
                );

        return http.build();
    }

    /**
     * ✅ Configuration CORS explicite en Java
     * Ceci garantit que CORS fonctionne même si application.yml échoue
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}