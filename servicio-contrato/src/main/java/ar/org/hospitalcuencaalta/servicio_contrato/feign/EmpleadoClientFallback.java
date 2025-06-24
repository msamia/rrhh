package ar.org.hospitalcuencaalta.servicio_contrato.feign;

import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmpleadoClientFallback implements EmpleadoClient {

    @Override
    public EmpleadoRegistryDto getById(Long id) {
        // Si servicio-empleado no está disponible, propagamos excepción para
        // que el servicio-contrato responda con error 503 y la SAGA pueda
        // manejar el fallo correctamente.
        log.error("[EmpleadoClientFallback] servicio-empleado no disponible al buscar id={}", id);
        throw new RuntimeException("Servicio-empleado no disponible");
    }
}
