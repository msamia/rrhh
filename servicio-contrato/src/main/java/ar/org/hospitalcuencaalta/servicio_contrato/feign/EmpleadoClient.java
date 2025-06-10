package ar.org.hospitalcuencaalta.servicio_contrato.feign;

import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para invocar a servicio-empleado.
 * Observa que en tu @FeignClient usas name="employee-service",
 * pero debe coincidir exactamente con el valor que Eureka registra para servicio-empleado.
 */
@FeignClient(
        name = "servicio-empleado",    // <— cambiar de "employee-service" a "servicio-empleado"
        fallback = EmpleadoClientFallback.class
)
public interface EmpleadoClient {

    @GetMapping("/api/empleados/{id}")
    EmpleadoRegistryDto getById(@PathVariable("id") Long id);
}
