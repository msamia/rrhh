package ar.org.hospitalcuencaalta.servidor_para_descubrimiento.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración mínima de OpenAPI para que el servidor de descubrimiento exponga una especificación vacía.
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Discovery Server", version = "v1"))
public class DiscoveryOpenApiConfig {
}
