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
                        .uri("lb://servicio-empleado"))
                .route("contrato_route", r -> r.path("/api/contratos/**")
                        .uri("lb://servicio-contrato"))
                .route("entrenamiento_route", r -> r.path("/api/capacitaciones/**", "/api/turnos/**", "/api/jornadas/**")
                        .uri("lb://servicio-entrenamiento"))
                .route("nomina_route", r -> r.path("/api/liquidaciones/**", "/api/conceptos/**")
                        .uri("lb://servicio-nomina"))
                .route("empleado_concepto_route", r -> r.path("/api/empleados/**/conceptos/**")
                        .uri("lb://servicio-nomina"))
                .route("consulta_route", r -> r.path("/api/consultas/**")
                        .uri("lb://servicio-consultas"))
                .route("saga_route", r -> r.path("/api/saga/**")
                        .uri("lb://servicio-orquestador"))
                .build();
    }
}

