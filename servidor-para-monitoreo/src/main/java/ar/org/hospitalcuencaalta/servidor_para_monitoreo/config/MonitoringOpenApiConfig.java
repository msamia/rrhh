package ar.org.hospitalcuencaalta.servidor_para_monitoreo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Monitoring Server", version = "v1"))
public class MonitoringOpenApiConfig {
}
