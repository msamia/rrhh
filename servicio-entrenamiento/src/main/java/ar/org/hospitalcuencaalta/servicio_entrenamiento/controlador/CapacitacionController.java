package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.CapacitacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.CapacitacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/capacitaciones")
@Tag(name = "Capacitaciones", description = "Gestión de capacitaciones")
public class CapacitacionController {
    @Autowired
    private CapacitacionService svc;

    @PostMapping
    @Operation(summary = "Crear capacitación", description = "Registra una nueva capacitación")
    public CapacitacionDto create(@RequestBody CapacitacionDto dto) {
        return svc.create(dto);
    }
    @GetMapping
    @Operation(summary = "Listar capacitaciones", description = "Obtiene todas las capacitaciones")
    public List<CapacitacionDto> all() {
        return svc.findAll();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de capacitación", description = "Obtiene información detallada")
    public CapacitacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}
