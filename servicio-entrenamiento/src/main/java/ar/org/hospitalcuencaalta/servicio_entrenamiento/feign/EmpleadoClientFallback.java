package ar.org.hospitalcuencaalta.servicio_entrenamiento.feign;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmpleadoClientFallback implements EmpleadoClient {
    @Override
    public EmpleadoRegistryDto getById(Long id) {
        log.error("[EmpleadoClientFallback] servicio-empleado no disponible al buscar id={}", id);
        throw new RuntimeException("Servicio-empleado no disponible");
    }
}
