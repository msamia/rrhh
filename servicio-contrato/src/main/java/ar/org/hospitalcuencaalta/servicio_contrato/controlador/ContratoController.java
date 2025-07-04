package ar.org.hospitalcuencaalta.servicio_contrato.controlador;

import ar.org.hospitalcuencaalta.servicio_contrato.servicio.ContratoService;
import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.ContratoLaboralDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {
    private final ContratoService svc;

    @PostMapping
    public ContratoLaboralDto create(@RequestBody ContratoLaboralDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    public List<ContratoLaboralDto> all() {
        return svc.findAll();
    }

    @PutMapping("/{id}")
    public ContratoLaboralDto update(@PathVariable Long id, @RequestBody ContratoLaboralDto dto) {
        return svc.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }

    @DeleteMapping("/empleado/{empleadoId}")
    public void deleteByEmpleadoId(@PathVariable Long empleadoId) {
        svc.deleteByEmpleadoId(empleadoId);
    }
}