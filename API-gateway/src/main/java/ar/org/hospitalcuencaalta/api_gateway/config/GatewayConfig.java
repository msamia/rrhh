package ar.org.hospitalcuencaalta.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("empleado_route", r -> r.path("/api/empleados/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-empleado"))
                .route("contrato_route", r -> r.path("/api/contratos/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-contrato"))
                .route("entrenamiento_route", r -> r.path("/api/capacitaciones/**", "/api/turnos/**", "/api/jornadas/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-entrenamiento"))
                .route("nomina_route", r -> r.path("/api/liquidaciones/**", "/api/conceptos/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-nomina"))
                .route("empleado_concepto_route", r -> r.order(-1)
                        .path("/api/empleados/{id}/conceptos/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-nomina"))
                .route("consulta_route", r -> r.path("/api/consultas/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-consultas"))
                .route("saga_route", r -> r.path("/api/saga/**")
                        .filters(f -> f.dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-orquestador"))
                .route("discovery_route", r -> r.path("/servidor-para-descubrimiento/**")
                        .filters(f -> f.stripPrefix(1)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("http://localhost:8761"))
                .route("openapi_ui_route", r -> r.path("/servicio-openapi-ui/**")
                        .filters(f -> f.stripPrefix(1)
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-openapi-ui"))
                .route("empleado_docs", r -> r.path("/servicio-empleado/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-empleado"))
                .route("contrato_docs", r -> r.path("/servicio-contrato/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-contrato"))
                .route("entrenamiento_docs", r -> r.path("/servicio-entrenamiento/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-entrenamiento"))
                .route("nomina_docs", r -> r.path("/servicio-nomina/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-nomina"))
                .route("consultas_docs", r -> r.path("/servicio-consultas/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-consultas"))
                .route("orquestador_docs", r -> r.path("/servicio-orquestador/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("lb://servicio-orquestador"))
                .route("discovery_docs", r -> r.path("/servidor-para-descubrimiento/v3/api-docs")
                        .filters(f -> f.stripPrefix(1).dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_UNIQUE"))
                        .uri("http://localhost:8761"))
                .build();
    }
}

