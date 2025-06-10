package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.JornadaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.JornadaProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/jornadas")
public class JornadaQueryController {
    @Autowired
    private JornadaProjectionRepository repo;

    @GetMapping
    public List<JornadaProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public JornadaProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
