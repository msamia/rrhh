package ar.org.hospitalcuencaalta.servicio_empleado.controlador;

import ar.org.hospitalcuencaalta.servicio_empleado.servicio.DepartamentoService;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDto;
import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.DepartamentoDetalleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService svc;

    @PostMapping
    public DepartamentoDto create(@PathVariable Long empleadoId,
                                  @RequestBody DepartamentoDto dto) {
        return svc.create(empleadoId, dto);
    }

    @PutMapping("/{id}")
    public DepartamentoDto update(@PathVariable Long empleadoId,
                                  @PathVariable Long id,
                                  @RequestBody DepartamentoDto dto) {
        return svc.update(empleadoId, id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long empleadoId,
                       @PathVariable Long id) {
        svc.delete(empleadoId, id);
    }

    @GetMapping("/{id}")
    public DepartamentoDetalleDto get(@PathVariable Long empleadoId,
                                      @PathVariable Long id) {
        return svc.get(empleadoId, id);
    }

    @GetMapping("/detalle")
    public Page<DepartamentoDetalleDto> allDetailed(@PathVariable Long empleadoId,
                                                    @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return svc.allDetailed(empleadoId, page, size);
    }

    @GetMapping
    public Page<DepartamentoDto> all(@PathVariable Long empleadoId,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        return svc.all(empleadoId, page, size);
    }
}

