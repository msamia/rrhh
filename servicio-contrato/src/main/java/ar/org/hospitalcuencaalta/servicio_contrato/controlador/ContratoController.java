package ar.org.hospitalcuencaalta.servicio_contrato.controlador;

import ar.org.hospitalcuencaalta.servicio_contrato.servicio.ContratoService;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
@Tag(name = "Contratos", description = "Operaciones sobre contratos laborales")
public class ContratoController {
    private final ContratoService svc;

    @PostMapping
    @Operation(summary = "Crear contrato", description = "Registra un nuevo contrato laboral")
    public ContratoLaboralDto create(@RequestBody ContratoLaboralDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    @Operation(summary = "Listar contratos", description = "Obtiene todos los contratos guardados")
    public List<ContratoLaboralDto> all() {
        return svc.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar contrato", description = "Modifica los datos de un contrato")
    public ContratoLaboralDto update(@PathVariable Long id, @RequestBody ContratoLaboralDto dto) {
        return svc.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contrato", description = "Elimina un contrato por su identificador")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }

    @DeleteMapping("/empleado/{empleadoId}")
    @Operation(summary = "Eliminar por empleado", description = "Borra contratos asociados al empleado dado")
    public void deleteByEmpleadoId(@PathVariable Long empleadoId) {
        svc.deleteByEmpleadoId(empleadoId);
    }
}