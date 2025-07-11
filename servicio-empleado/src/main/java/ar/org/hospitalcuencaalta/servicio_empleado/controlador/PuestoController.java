package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.PuestoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/puestos")
@RequiredArgsConstructor
@Tag(name = "Puestos", description = "Gestión de puestos del empleado")
public class PuestoController {

    private final PuestoService svc;

    @PostMapping
    @Operation(summary = "Crear puesto", description = "Agrega un puesto al empleado")
    public PuestoDto create(@PathVariable Long empleadoId,
                            @RequestBody PuestoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar puesto", description = "Modifica un puesto del empleado")
    public PuestoDto update(@PathVariable Long empleadoId,
                            @PathVariable Long id,
                            @RequestBody PuestoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar puesto", description = "Quita un puesto del empleado")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de puesto", description = "Obtiene un puesto específico")
    public PuestoDetalleDto get(@PathVariable Long empleadoId,
                                @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    @Operation(summary = "Puestos detallados", description = "Lista puestos del empleado con detalle")
    public Page<PuestoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    @Operation(summary = "Listar puestos", description = "Devuelve los puestos asociados al empleado")
    public Page<PuestoDto> all(@PathVariable Long empleadoId,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

