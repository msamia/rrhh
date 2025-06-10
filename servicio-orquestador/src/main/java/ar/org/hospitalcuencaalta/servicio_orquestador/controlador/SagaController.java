package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaEmpleadoContratoRequest;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.SagaStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.UUID;

/**
 * Controlador REST que expone el endpoint para iniciar la SAGA
 * de creación de empleado + contrato.
 */
@RestController
@RequestMapping("/api/saga")
public class SagaController {

    private final StateMachineFactory<Estados, Eventos> stateMachineFactory;

    @Autowired
    public SagaController(StateMachineFactory<Estados, Eventos> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @PostMapping("/empleado-contrato")
    public SagaStatusResponse iniciarSaga(@RequestBody SagaEmpleadoContratoRequest request) {
        // 1) Crear un nuevo StateMachine de SAGA por cada petición
        StateMachine<Estados, Eventos> stateMachine =
                stateMachineFactory.getStateMachine(UUID.randomUUID().toString());
        stateMachine.start();

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
}
