package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.PuestoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.PuestoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoService svc;

    @PostMapping
    public PuestoDto create(@PathVariable Long empleadoId,
                            @RequestBody PuestoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    public PuestoDto update(@PathVariable Long empleadoId,
                            @PathVariable Long id,
                            @RequestBody PuestoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    public PuestoDetalleDto get(@PathVariable Long empleadoId,
                                @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    public Page<PuestoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                              @RequestParam(defaultValue = "0") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    public Page<PuestoDto> all(@PathVariable Long empleadoId,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

