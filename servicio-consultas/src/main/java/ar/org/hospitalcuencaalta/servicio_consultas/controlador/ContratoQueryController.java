package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ContratoProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.ContratoProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@Tag(name = "Consulta Contratos", description = "Consultas de contratos laborales")
public class ContratoQueryController {
    @Autowired
    private ContratoProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar contratos", description = "Devuelve todos los contratos registrados")
    public List<ContratoProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Contrato por id", description = "Obtiene un contrato en particular")
    public ContratoProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}
