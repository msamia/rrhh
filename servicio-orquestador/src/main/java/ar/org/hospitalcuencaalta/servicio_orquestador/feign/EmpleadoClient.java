package ar.org.hospitalcuencaalta.servicio_orquestador.feign;

import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Cliente Feign para el microservicio “servicio-empleado”.
 * Endpoints típicos:
 *   - POST   /api/empleados      → create
 *   - PUT    /api/empleados/{id} → update
 *   - DELETE /api/empleados/{id} → delete
 */
@FeignClient(
        name = "servicio-empleado",
        fallback = EmpleadoClientFallback.class
)
public interface EmpleadoClient {

    @PostMapping("/api/empleados")
    EmpleadoDto create(@RequestBody EmpleadoDto dto);

    @PutMapping("/api/empleados/{id}")
    EmpleadoDto update(@PathVariable("id") Long id, @RequestBody EmpleadoDto dto);

    @DeleteMapping("/api/empleados/{id}")
    void delete(@PathVariable("id") Long id);

    /**
     * Busca un empleado por documento.
     * Debe retornar 200 + EmpleadoDto si existe, o 404 si no existe.
     */
    @GetMapping("/api/empleados/documento/{documento}")
    EmpleadoDto findByDocumento(@PathVariable("documento") String documento);
}
