package ar.org.hospitalcuencaalta.servidor_para_descubrimiento.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Minimal OpenAPI configuration so the discovery server exposes an empty spec.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Discovery Server", version = "v1"))
public class DiscoveryOpenApiConfig {
}
