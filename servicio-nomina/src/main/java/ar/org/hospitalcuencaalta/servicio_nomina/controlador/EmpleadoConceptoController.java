package ar.org.hospitalcuencaalta.servicio_nomina.controlador;

import ar.org.hospitalcuencaalta.servicio_nomina.servicio.EmpleadoConceptoService;
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
    public void asignar(@PathVariable Long empleadoId, @PathVariable Long conceptoId) {
        svc.asignarConcepto(empleadoId, conceptoId);
    }
}
