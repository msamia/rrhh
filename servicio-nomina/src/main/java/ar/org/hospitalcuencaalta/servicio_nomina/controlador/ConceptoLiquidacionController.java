package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.ConceptoLiquidacionService;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/conceptos")
@Tag(name = "Conceptos", description = "Administración de conceptos de liquidación")
public class ConceptoLiquidacionController {
    @Autowired
    private ConceptoLiquidacionService svc;

    @PostMapping
    @Operation(summary = "Crear concepto", description = "Registra un concepto de liquidación")
    public ConceptoLiquidacionDto create(@RequestBody ConceptoLiquidacionDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    @Operation(summary = "Listar conceptos", description = "Obtiene todos los conceptos disponibles")
    public List<ConceptoLiquidacionDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de concepto", description = "Devuelve un concepto específico")
    public ConceptoLiquidacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}
