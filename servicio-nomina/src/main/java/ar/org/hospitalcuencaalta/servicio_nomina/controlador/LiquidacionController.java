package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.LiquidacionService;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDetalleDto;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.LiquidacionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/liquidaciones")
public class LiquidacionController {
    @Autowired
    private LiquidacionService svc;

    @PostMapping
    public LiquidacionDto create(@RequestBody LiquidacionDto dto) {
        return svc.create(dto);
    }

    @GetMapping
    public List<LiquidacionDto> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public LiquidacionDetalleDto get(@PathVariable Long id) {
        return svc.getDetalle(id);
    }
}
