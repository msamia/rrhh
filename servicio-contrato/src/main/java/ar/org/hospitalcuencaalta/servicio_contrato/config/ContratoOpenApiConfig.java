package ar.org.hospitalcuencaalta.servicio_contrato.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para el Servicio Contrato.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Contrato",
                version = "v1",
                description = "Maneja la gestión de contratos de los empleados.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class ContratoOpenApiConfig {
}
