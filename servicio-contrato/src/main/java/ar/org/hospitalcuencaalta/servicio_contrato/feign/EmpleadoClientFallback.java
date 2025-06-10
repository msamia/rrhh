package ar.org.hospitalcuencaalta.servicio_contrato.feign;

import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoClientFallback implements EmpleadoClient {

    @Override
    public EmpleadoRegistryDto getById(Long id) {
        // Si servicio-empleado no está disponible, devolvemos un DTO “por defecto”
        return EmpleadoRegistryDto.builder()
                .id(id)
                .legajo("N/D")
                .nombre("N/D")
                .apellido("N/D")
                .build();
    }
}
