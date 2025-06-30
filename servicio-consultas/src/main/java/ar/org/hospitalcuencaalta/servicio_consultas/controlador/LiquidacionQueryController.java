package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.LiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.LiquidacionProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Read model access for payroll liquidations.
 */
@RestController
@RequestMapping("/api/liquidaciones")
public class LiquidacionQueryController {
    @Autowired
    private LiquidacionProjectionRepository repo;

    @GetMapping
    public List<LiquidacionProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public LiquidacionProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
