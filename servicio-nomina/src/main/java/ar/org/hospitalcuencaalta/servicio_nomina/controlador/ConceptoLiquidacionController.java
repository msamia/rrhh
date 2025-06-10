package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.ConceptoLiquidacionService;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.ConceptoLiquidacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conceptos")
public class ConceptoLiquidacionController {
    @Autowired
    private ConceptoLiquidacionService svc;

    @PostMapping
    public ConceptoLiquidacionDto create(@RequestBody ConceptoLiquidacionDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    public List<ConceptoLiquidacionDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ConceptoLiquidacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}
