package ar.org.hospitalcuencaalta.servicio_orquestador;

import ar.org.hospitalcuencaalta.servicio_orquestador.accion.CompensacionSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.ContratoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.EmpleadoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.SagaCompletionActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.config.SagaStateMachineConfig;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = SagaStateMachineConfig.class)
@TestConstructor(autowireMode = AutowireMode.ALL)
class SagaStateMachineConfigTest {

    @Autowired
    private StateMachineFactory<Estados, Eventos> stateMachineFactory;

    @MockitoBean private EmpleadoSagaActions empleadoActions;
    @MockitoBean private ContratoSagaActions contratoActions;
    @MockitoBean private CompensacionSagaActions compensacionActions;
    @MockitoBean private SagaCompletionActions completionActions;
    @MockitoBean private SagaStateService sagaStateService;

    @Test
    void contratoCreadoTriggersFinalizada() {
        StateMachine<Estados, Eventos> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().block();

        Message<Eventos> startMsg = MessageBuilder.withPayload(Eventos.SOLICITAR_CREAR_EMPLEADO).build();
        sm.sendEvent(Mono.just(startMsg)).blockLast();
        sm.sendEvent(Mono.just(MessageBuilder.withPayload(Eventos.EMPLEADO_CREADO).build())).blockLast();
        sm.sendEvent(Mono.just(MessageBuilder.withPayload(Eventos.CONTRATO_CREADO).build())).blockLast();

        assertEquals(Estados.FINALIZADA, sm.getState().getId());
        verify(sagaStateService, atLeastOnce()).save(sm);
    }

    @Test
    void contratoFallidoEnActualizacionTerminaRevertida() {
        StateMachine<Estados, Eventos> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().block();

        // Simular camino de actualización hasta el fallo del contrato
        sm.sendEvent(Mono.just(MessageBuilder
                .withPayload(Eventos.SOLICITAR_ACTUALIZAR_EMPLEADO)
                .build())).blockLast();
        sm.sendEvent(Mono.just(MessageBuilder
                .withPayload(Eventos.EMPLEADO_ACTUALIZADO)
                .build())).blockLast();
        sm.sendEvent(Mono.just(MessageBuilder
                .withPayload(Eventos.SOLICITAR_ACTUALIZAR_CONTRATO)
                .build())).blockLast();
        sm.sendEvent(Mono.just(MessageBuilder
                .withPayload(Eventos.CONTRATO_FALLIDO)
                .build())).blockLast();

        assertEquals(Estados.REVERTIDA, sm.getState().getId());
        verify(sagaStateService, atLeastOnce()).save(sm);
    }
}

