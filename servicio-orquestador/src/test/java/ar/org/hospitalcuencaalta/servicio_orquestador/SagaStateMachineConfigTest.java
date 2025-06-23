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
import ar.org.hospitalcuencaalta.comunes.statemachine.EventosSM;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

        EventosSM.enviar(sm, Eventos.SOLICITAR_CREAR_EMPLEADO);
        EventosSM.enviar(sm, Eventos.EMPLEADO_CREADO);
        EventosSM.enviar(sm, Eventos.CONTRATO_CREADO);

        assertEquals(Estados.FINALIZADA, sm.getState().getId());
        verify(sagaStateService, atLeastOnce()).save(sm);
    }

    @Test
    void contratoFallidoEnActualizacionTerminaRevertida() {
        StateMachine<Estados, Eventos> sm = stateMachineFactory.getStateMachine();
        sm.startReactively().block();

        // Simular camino de actualización hasta el fallo del contrato
        EventosSM.enviar(sm, Eventos.SOLICITAR_ACTUALIZAR_EMPLEADO);
        EventosSM.enviar(sm, Eventos.EMPLEADO_ACTUALIZADO);
        EventosSM.enviar(sm, Eventos.SOLICITAR_ACTUALIZAR_CONTRATO);
        EventosSM.enviar(sm, Eventos.CONTRATO_FALLIDO);

        assertEquals(Estados.FALLIDA, sm.getState().getId());
        verify(sagaStateService, atLeastOnce()).save(sm);
    }
}

