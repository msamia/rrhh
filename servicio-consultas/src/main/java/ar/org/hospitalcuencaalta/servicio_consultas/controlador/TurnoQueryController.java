package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.TurnoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.TurnoProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@Tag(name = "Consulta Turnos", description = "Consultas de turnos asignados")
public class TurnoQueryController {
    @Autowired
    private TurnoProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar turnos", description = "Devuelve todos los turnos asignados")
    public List<TurnoProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Turno por id", description = "Obtiene el turno especificado")
    public TurnoProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
