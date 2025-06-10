package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.EmpleadoProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoQueryController {
    @Autowired
    private EmpleadoProjectionRepository repo;

    @GetMapping
    public List<EmpleadoProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public EmpleadoProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
