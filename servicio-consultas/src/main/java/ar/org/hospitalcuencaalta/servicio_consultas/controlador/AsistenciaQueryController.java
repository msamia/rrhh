package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.AsistenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.AsistenciaProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaQueryController {
    @Autowired
    private AsistenciaProjectionRepository repo;

    @GetMapping
    public List<AsistenciaProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public AsistenciaProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}

