package ar.org.hospitalcuencaalta.servicio_orquestador.feign;

import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback para EmpleadoClient.
 * Solo cubre los casos en que el servicio-empleado está indisponible o arroja 5xx.
 * NOTA: Si el método findByDocumento lanza FeignException.NotFound, ese NO
 * será atrapado aquí, porque FeignException.NotFound es una excepción 404
 * que no activa el fallback (se propaga al caller para que verifique “no existe”).
 */
@Slf4j
@Component
public class EmpleadoClientFallback implements EmpleadoClient {

    @Override
    public EmpleadoDto create(EmpleadoDto dto) {
        throw new RuntimeException("Fallback: servicio-empleado no disponible al crear empleado");
    }

    @Override
    public EmpleadoDto update(Long id, EmpleadoDto dto) {
        throw new RuntimeException("Fallback: servicio-empleado no disponible al actualizar empleado con id=" + id);
    }

    @Override
    public void delete(Long id) {
        throw new RuntimeException("Fallback: servicio-empleado no disponible al eliminar empleado con id=" + id);
    }

    @Override
    public EmpleadoDto findByDocumento(String documento) {
        log.error("[EmpleadoClientFallback] findByDocumento({}) - Servicio-empleado no disponible", documento);
        // Como fallback, propagamos excepción genérica. El llamador interpretará esto
        // como “error en llamada” y se disparará el CircuitBreaker o fallback de la SAGA.
        throw new RuntimeException("Servicio-empleado indisponible. No se pudo verificar existencia de documento.");
    }
}
