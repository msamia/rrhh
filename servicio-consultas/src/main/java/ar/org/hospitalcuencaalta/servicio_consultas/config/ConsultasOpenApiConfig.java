package ar.org.hospitalcuencaalta.servicio_consultas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para el Servicio Consultas.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Consultas",
                version = "v1",
                description = "Expone endpoints de consulta alimentados por otros servicios.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class ConsultasOpenApiConfig {
}
