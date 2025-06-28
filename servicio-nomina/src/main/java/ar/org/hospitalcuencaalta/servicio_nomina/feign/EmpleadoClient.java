package ar.org.hospitalcuencaalta.servicio_nomina.feign;

import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.EmpleadoRegistryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para servicio-empleado. Permite verificar la existencia
 * de un empleado por id antes de generar una liquidación.
 */
@FeignClient(
        name = "servicio-empleado",
        url = "${empleado.service.url:lb://servicio-empleado}",
        fallback = EmpleadoClientFallback.class
)
public interface EmpleadoClient {

    @GetMapping("/api/empleados/{id}")
    EmpleadoRegistryDto getById(@PathVariable("id") Long id);
}
