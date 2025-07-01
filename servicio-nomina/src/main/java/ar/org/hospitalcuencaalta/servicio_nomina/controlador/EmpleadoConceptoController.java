package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.EmpleadoConceptoService;
import ar.org.hospitalcuencaalta.servicio_nomina.web.dto.EmpleadoConceptoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empleados/{empleadoId}/conceptos")
public class EmpleadoConceptoController {
    @Autowired
    private EmpleadoConceptoService svc;

    @PostMapping("/{conceptoId}")
    public EmpleadoConceptoDto asignar(@PathVariable Long empleadoId, @PathVariable Long conceptoId) {
        return svc.asignarConcepto(empleadoId, conceptoId);
    }
}
