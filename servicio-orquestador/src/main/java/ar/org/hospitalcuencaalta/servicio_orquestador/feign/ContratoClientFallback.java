package ar.org.hospitalcuencaalta.servicio_orquestador.feign;

import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
import org.springframework.stereotype.Component;

/**
 * Fallback para ContratoClient: implementa create, update y delete.
 */
@Component
public class ContratoClientFallback implements ContratoClient {

    @Override
    public ContratoLaboralDto create(ContratoLaboralDto dto) {
        throw new RuntimeException("Fallback: servicio-contrato no disponible al crear contrato");
    }

    @Override
    public ContratoLaboralDto update(Long id, ContratoLaboralDto dto) {
        throw new RuntimeException("Fallback: servicio-contrato no disponible al actualizar contrato con id=" + id);
    }

    @Override
    public void delete(Long id) {
        throw new RuntimeException("Fallback: servicio-contrato no disponible al eliminar contrato con id=" + id);
    }
}
