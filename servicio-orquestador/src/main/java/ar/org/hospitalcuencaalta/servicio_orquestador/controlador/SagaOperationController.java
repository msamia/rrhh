package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.operacion.SagaOperation;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaOperationService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/saga/operaciones")
@Tag(name = "Operaciones de SAGA", description = "Historial de pasos ejecutados")
public class SagaOperationController {

    private final SagaOperationService service;

    public SagaOperationController(SagaOperationService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Historial", description = "Listado de operaciones realizadas por una SAGA")
    public List<SagaOperation> all(@RequestParam(value = "sagaId", required = false) Long sagaId) {
        if (sagaId != null) {
            return service.findBySagaId(sagaId);
        }
        return service.findAll();
    }
}
