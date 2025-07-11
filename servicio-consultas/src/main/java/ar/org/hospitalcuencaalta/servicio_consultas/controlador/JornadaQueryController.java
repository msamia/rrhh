package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.JornadaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.JornadaProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/jornadas")
@Tag(name = "Consulta Jornadas", description = "Consultas de jornadas laborales")
public class JornadaQueryController {
    @Autowired
    private JornadaProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar jornadas", description = "Obtiene todas las jornadas laborales")
    public List<JornadaProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Jornada por id", description = "Devuelve la jornada laboral solicitada")
    public JornadaProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
