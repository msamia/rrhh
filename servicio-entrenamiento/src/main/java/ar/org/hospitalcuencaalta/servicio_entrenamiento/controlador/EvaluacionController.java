package ar.org.hospitalcuencaalta.servicio_entrenamiento.controlador;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.servicio.EvaluacionService;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EvaluacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {
    @Autowired
    private EvaluacionService svc;

    @PostMapping
    public EvaluacionDto create(@RequestBody EvaluacionDto dto) {
        return svc.create(dto);
    }
    @GetMapping
    public List<EvaluacionDto> all() {
        return svc.findAll();
    }
    @GetMapping("/{id}")
    public EvaluacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}