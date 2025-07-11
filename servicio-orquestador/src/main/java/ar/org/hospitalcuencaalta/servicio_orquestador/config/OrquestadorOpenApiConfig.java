package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para el Servicio Orquestador.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Orquestador",
                version = "v1",
                description = "Coordina la creación y actualización de empleados y contratos mediante SAGA.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class OrquestadorOpenApiConfig {
}
