package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ContratoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ContratoProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoQueryController {
    @Autowired
    private ContratoProjectionRepository repo;

    @GetMapping
    public List<ContratoProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ContratoProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
