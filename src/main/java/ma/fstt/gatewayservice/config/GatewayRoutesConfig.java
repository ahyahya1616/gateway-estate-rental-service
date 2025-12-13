//package ma.fstt.gatewayservice.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayRoutesConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                // Auth Server OAuth2 (routes publiques)
//                .route("auth-server-public", r -> r
//                        .path("/oauth2/**", "/.well-known/**")
//                        .uri("http://localhost:9000"))
//
//                // Authentication Service - MetaMask (routes publiques)
//                .route("auth-service-public", r -> r
//                        .path("/api/auth/**")
//                        .uri("lb://AUTHENTIFICATION-SERVICE"))
//
//                // User Management Service (routes protégées)
//                .route("user-service", r -> r
//                        .path("/api/users/**")
//                        .filters(f -> f.tokenRelay())
//                        .uri("lb://USER-MANAGEMENT-SERVICE"))
//
//                // Auction Service (routes protégées)
//                .route("auction-service", r -> r
//                        .path("/api/auctions/**")
//                        .filters(f -> f.tokenRelay())
//                        .uri("lb://AUCTION-SERVICE"))
//
//                .route("property-service", r->r
//                        .path("/api/properties")
//                        .uri("lb://PROPERTY-MICROSERVICE")
//                )
//
//                .route("rentalAgreement-service", r->r
//                        .path("/api/v1/**")
//                        .uri("lb://RENTAL-AGREEMENT-MICROSERVICE")
//                )
//
//                // Bid Service (routes protégées)
//                .route("bid-service", r -> r
//                        .path("/api/bids/**")
//                        .filters(f -> f.tokenRelay())
//                        .uri("lb://BID-SERVICE"))
//
//                .build();
//    }
//}