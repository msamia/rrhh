package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaEmpleadoContratoRequest;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaStatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST que expone el endpoint para iniciar la SAGA
 * de creación de empleado + contrato.
 */
@RestController
@RequestMapping("/api/saga")
public class SagaController {

    private final StateMachineFactory<Estados, Eventos> stateMachineFactory;
    private final SagaStateService sagaStateService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SagaController(StateMachineFactory<Estados, Eventos> stateMachineFactory,
                          SagaStateService sagaStateService,
                          ObjectMapper objectMapper) {
        this.stateMachineFactory = stateMachineFactory;
        this.sagaStateService = sagaStateService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/empleado-contrato")
    public SagaStatusResponse iniciarSaga(@RequestBody SagaEmpleadoContratoRequest request) {
        // 1) Crear un nuevo StateMachine de SAGA por cada petición
        StateMachine<Estados, Eventos> stateMachine =
                stateMachineFactory.getStateMachine(UUID.randomUUID().toString());

        // Iniciar la máquina de estados. Algunas implementaciones devuelven
        // un Mono desde `startReactively()`; otras retornan null, lo cual
        // provocaba un NPE en los tests al llamar `block()`.  Intentamos el
        // arranque reactivo y, si es null, caemos al inicio sincrónico.
        var startMono = stateMachine.startReactively();
        if (startMono != null) {
            startMono.block();
        } else {
            stateMachine.start();
        }
        sagaStateService.save(stateMachine);

        // 2) Guardar DTOs en extendedState
        stateMachine.getExtendedState().getVariables()
                .put("empleadoDto", request.getEmpleado());
        stateMachine.getExtendedState().getVariables()
                .put("contratoDto", request.getContrato());

        // 3) Enviar evento SOLICITAR_CREAR_EMPLEADO
        Message<Eventos> mensaje = MessageBuilder.withPayload(Eventos.SOLICITAR_CREAR_EMPLEADO)
                .setHeader("sagaId", stateMachine.getUuid().toString())
                .build();
        stateMachine.sendEvents(Flux.just(mensaje)).subscribe();

        // 4) Devolver estado inicial de la SAGA
        return SagaStatusResponse.builder()
                .sagaId(stateMachine.getUuid().toString())
                .estadoActual(stateMachine.getState().getId().name())
                .idEmpleadoCreado(null)
                .idContratoCreado(null)
                .mensajeError(null)
                .timestampInicio(Instant.now())
                .timestampFin(null)
                .build();
    }

    @GetMapping("/empleado-contrato/{id}")
    public ResponseEntity<SagaStatusResponse> obtenerEstado(@PathVariable("id") UUID id) {
        return sagaStateService.findById(id)
                .map(state -> {
                    Map<String, Object> ext;
                    try {
                        ext = objectMapper.readValue(state.getExtendedState(), Map.class);
                    } catch (Exception e) {
                        ext = Map.of();
                    }

                    Long empId = ext.get("idEmpleado") instanceof Number
                            ? ((Number) ext.get("idEmpleado")).longValue() : null;
                    Long conId = ext.get("idContrato") instanceof Number
                            ? ((Number) ext.get("idContrato")).longValue() : null;
                    String msg = ext.get("mensajeError") instanceof String
                            ? (String) ext.get("mensajeError") : null;

                    return SagaStatusResponse.builder()
                            .sagaId(state.getSagaId().toString())
                            .estadoActual(state.getEstado().name())
                            .idEmpleadoCreado(empId)
                            .idContratoCreado(conId)
                            .mensajeError(msg)
                            .timestampInicio(null)
                            .timestampFin(state.getUpdatedAt())
                            .build();
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
