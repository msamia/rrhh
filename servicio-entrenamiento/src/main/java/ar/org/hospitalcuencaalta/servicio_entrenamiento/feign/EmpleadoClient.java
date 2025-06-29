package ar.org.hospitalcuencaalta.servicio_entrenamiento.feign;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-empleado", fallback = EmpleadoClientFallback.class)
public interface EmpleadoClient {
    @GetMapping("/api/empleados/{id}")
    EmpleadoRegistryDto getById(@PathVariable("id") Long id);
}
