package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.DocumentacionService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DocumentacionDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/documentaciones")
@RequiredArgsConstructor
public class DocumentacionController {

    private final DocumentacionService svc;

    @PostMapping
    public DocumentacionDto create(@PathVariable Long empleadoId,
                                   @RequestBody DocumentacionDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    public DocumentacionDto update(@PathVariable Long empleadoId,
                                   @PathVariable Long id,
                                   @RequestBody DocumentacionDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    public DocumentacionDetalleDto get(@PathVariable Long empleadoId,
                                       @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    public Page<DocumentacionDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    public Page<DocumentacionDto> all(@PathVariable Long empleadoId,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

