package ar.org.hospitalcuencaalta.servicio_empleado.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI para documentar el Servicio Empleado.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Servicio Empleado",
                version = "v1",
                description = "Expone las operaciones CRUD de empleados.",
                contact = @Contact(name = "Equipo RRHH", email = "rrhh@example.com")
        )
)
public class EmpleadoOpenApiConfig {
}
