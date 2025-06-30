package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ConceptoLiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ConceptoLiquidacionProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Exposes read-only endpoints for concepts from the read model. This lets us
 * verify that updates published by servicio-nomina appear here through Kafka
 * listeners.
 */
@RestController
@RequestMapping("/api/conceptos")
public class ConceptoLiquidacionQueryController {
    @Autowired
    private ConceptoLiquidacionProjectionRepository repo;

    @GetMapping
    public List<ConceptoLiquidacionProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ConceptoLiquidacionProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
