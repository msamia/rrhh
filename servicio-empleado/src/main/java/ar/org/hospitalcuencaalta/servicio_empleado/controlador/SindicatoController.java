package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.SindicatoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/sindicatos")
@RequiredArgsConstructor
@Tag(name = "Sindicatos", description = "Gestión de sindicatos del empleado")
public class SindicatoController {

    private final SindicatoService svc;

    @PostMapping
    @Operation(summary = "Crear sindicato", description = "Asocia un sindicato al empleado")
    public SindicatoDto create(@PathVariable Long empleadoId,
                               @RequestBody SindicatoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sindicato", description = "Modifica un sindicato del empleado")
    public SindicatoDto update(@PathVariable Long empleadoId,
                               @PathVariable Long id,
                               @RequestBody SindicatoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sindicato", description = "Quita el sindicato indicado")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de sindicato", description = "Obtiene datos de un sindicato")
    public SindicatoDetalleDto get(@PathVariable Long empleadoId,
                                   @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    @Operation(summary = "Sindicatos detallados", description = "Lista los sindicatos del empleado con detalle")
    public Page<SindicatoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                 @RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    @Operation(summary = "Listar sindicatos", description = "Devuelve los sindicatos del empleado")
    public Page<SindicatoDto> all(@PathVariable Long empleadoId,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

