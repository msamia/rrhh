package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.operacion.SagaOperation;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaOperationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saga/operaciones")
public class SagaOperationController {

    private final SagaOperationService service;

    public SagaOperationController(SagaOperationService service) {
        this.service = service;
    }

    @GetMapping
    public List<SagaOperation> all(@RequestParam(value = "sagaId", required = false) Long sagaId) {
        if (sagaId != null) {
            return service.findBySagaId(sagaId);
        }
        return service.findAll();
    }
}
