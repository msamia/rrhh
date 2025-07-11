package ar.org.hospitalcuencaalta.servicio_entrenamiento.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para el Servicio Entrenamiento.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Entrenamiento",
                version = "v1",
                description = "Gestiona cursos y capacitaciones del personal.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class EntrenamientoOpenApiConfig {
}
