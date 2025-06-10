package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.CapacitacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/capacitaciones")
public class CapacitacionController {
    @Autowired
    private CapacitacionService svc;

    @PostMapping
    public CapacitacionDto create(@RequestBody CapacitacionDto dto) {
        return svc.create(dto);
    }
    @GetMapping
    public List<CapacitacionDto> all() {
        return svc.findAll();
    }
    @GetMapping("/{id}")
    public CapacitacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}
