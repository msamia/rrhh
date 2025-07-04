package ar.org.hospitalcuencaalta.api_gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Minimal OpenAPI configuration so the gateway exposes an empty spec.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "v1"))
public class GatewayOpenApiConfig {
}
