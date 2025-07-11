package ar.org.hospitalcuencaalta.servicio_nomina.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para el Servicio Nómina.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Nómina",
                version = "v1",
                description = "Procesa pagos y recibos salariales.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class NominaOpenApiConfig {
}
