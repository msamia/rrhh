package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.SindicatoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.SindicatoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/sindicatos")
@RequiredArgsConstructor
public class SindicatoController {

    private final SindicatoService svc;

    @PostMapping
    public SindicatoDto create(@PathVariable Long empleadoId,
                               @RequestBody SindicatoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    public SindicatoDto update(@PathVariable Long empleadoId,
                               @PathVariable Long id,
                               @RequestBody SindicatoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    public SindicatoDetalleDto get(@PathVariable Long empleadoId,
                                   @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    public Page<SindicatoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                 @RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    public Page<SindicatoDto> all(@PathVariable Long empleadoId,
                                  @RequestParam(defaultValue = "0") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

