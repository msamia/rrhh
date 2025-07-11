package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.EvaluacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@Tag(name = "Evaluaciones", description = "Gestión de evaluaciones")
public class EvaluacionController {
    @Autowired
    private EvaluacionService svc;

    @PostMapping
    @Operation(summary = "Crear evaluación", description = "Registra una evaluación de capacitación")
    public EvaluacionDto create(@RequestBody EvaluacionDto dto) {
        return svc.create(dto);
    }
    @GetMapping
    @Operation(summary = "Listar evaluaciones", description = "Muestra todas las evaluaciones registradas")
    public List<EvaluacionDto> all() {
        return svc.findAll();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Detalle de evaluación", description = "Obtiene los detalles de una evaluación")
    public EvaluacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}