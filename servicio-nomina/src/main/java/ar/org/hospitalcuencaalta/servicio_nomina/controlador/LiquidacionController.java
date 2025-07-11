package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.LiquidacionService;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/liquidaciones")
@Tag(name = "Liquidaciones", description = "Procesos de liquidación de salarios")
public class LiquidacionController {
    @Autowired
    private LiquidacionService svc;

    @PostMapping
    @Operation(summary = "Crear liquidación", description = "Genera una nueva liquidación")
    public LiquidacionDto create(@RequestBody LiquidacionDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    @Operation(summary = "Listar liquidaciones", description = "Obtiene todas las liquidaciones")
    public List<LiquidacionDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalle de liquidación", description = "Devuelve la liquidación indicada")
    public LiquidacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }

    @PostMapping("/{id}/calcular")
    @Operation(summary = "Calcular nómina", description = "Procesa la liquidación de un periodo")
    public LiquidacionDetalleDto calcular(@PathVariable Long id) {
        return svc.calcularNomina(id);
    }
}
