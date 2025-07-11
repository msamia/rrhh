package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.DocumentacionService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/documentaciones")
@RequiredArgsConstructor
@Tag(name = "Documentaciones", description = "Gestión de documentación del empleado")
public class DocumentacionController {

    private final DocumentacionService svc;

    @PostMapping
    @Operation(summary = "Crear documentación", description = "Agrega un registro documental al empleado")
    public DocumentacionDto create(@PathVariable Long empleadoId,
                                   @RequestBody DocumentacionDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar documentación", description = "Modifica un registro documental del empleado")
    public DocumentacionDto update(@PathVariable Long empleadoId,
                                   @PathVariable Long id,
                                   @RequestBody DocumentacionDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar documentación", description = "Quita un registro documental del empleado")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de documentación", description = "Obtiene la documentación indicada")
    public DocumentacionDetalleDto get(@PathVariable Long empleadoId,
                                       @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    @Operation(summary = "Documentación detallada", description = "Lista la documentación del empleado con detalle")
    public Page<DocumentacionDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    @Operation(summary = "Listar documentación", description = "Devuelve la documentación asociada al empleado")
    public Page<DocumentacionDto> all(@PathVariable Long empleadoId,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

