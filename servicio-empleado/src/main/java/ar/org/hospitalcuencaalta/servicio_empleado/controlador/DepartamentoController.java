package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.DepartamentoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/departamentos")
@RequiredArgsConstructor
@Tag(name = "Departamentos", description = "Gestión de departamentos del empleado")
public class DepartamentoController {

    private final DepartamentoService svc;

    @PostMapping
    @Operation(summary = "Crear departamento", description = "Agrega un departamento al empleado")
    public DepartamentoDto create(@PathVariable Long empleadoId,
                                  @RequestBody DepartamentoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar departamento", description = "Modifica un departamento del empleado")
    public DepartamentoDto update(@PathVariable Long empleadoId,
                                  @PathVariable Long id,
                                  @RequestBody DepartamentoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar departamento", description = "Quita un departamento del empleado")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de departamento", description = "Obtiene un departamento específico")
    public DepartamentoDetalleDto get(@PathVariable Long empleadoId,
                                      @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    @Operation(summary = "Departamentos detallados", description = "Lista departamentos del empleado con detalle")
    public Page<DepartamentoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    @Operation(summary = "Listar departamentos", description = "Lista departamentos del empleado")
    public Page<DepartamentoDto> all(@PathVariable Long empleadoId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

