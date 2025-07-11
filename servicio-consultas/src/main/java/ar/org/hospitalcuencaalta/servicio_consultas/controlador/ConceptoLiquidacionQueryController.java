package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ConceptoLiquidacionProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ConceptoLiquidacionProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Expone puntos finales de solo lectura para conceptos del modelo de lectura.
 * Esto nos permite verificar que las actualizaciones publicadas por servicio-nomina
 * aparezcan aquí mediante los escuchas de Kafka.
 */
@RestController
@RequestMapping("/api/conceptos")
@Tag(name = "Consulta Conceptos", description = "Consultas de conceptos de liquidación")
public class ConceptoLiquidacionQueryController {
    @Autowired
    private ConceptoLiquidacionProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar conceptos", description = "Devuelve todos los conceptos de liquidación")
    public List<ConceptoLiquidacionProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Concepto por id", description = "Obtiene un concepto de liquidación")
    public ConceptoLiquidacionProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
