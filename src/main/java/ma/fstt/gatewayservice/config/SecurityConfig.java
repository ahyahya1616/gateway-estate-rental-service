package ma.fstt.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/auth/metamask/nonce").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/metamask/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/auth/metamask/refresh").permitAll()
                        .pathMatchers("/ws-notifications/**").permitAll()
                        .pathMatchers("/oauth2/**", "/.well-known/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/properties/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/rooms/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/property-microservice/properties/room-images/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users/wallet/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .pathMatchers("/actuator/health", "/actuator/info" , "/actuator/env").permitAll()

                        // Routes protégées
                        .pathMatchers("/api/property-microservice/**").authenticated()
                        .pathMatchers("/api/users/**").authenticated()
                        .pathMatchers("/api/rentalAgreement-microservice/**").authenticated()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. Origines autorisées (AWS ready)
        config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));

        // 2. MÉTHODES AUTORISÉES (Crucial pour éviter l'erreur CORS sur 401)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 3. HEADERS AUTORISÉS
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));

        // 4. HEADERS EXPOSÉS
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));

        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}