package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import ar.org.hospitalcuencaalta.servicio_orquestador.accion.CompensacionSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.ContratoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.EmpleadoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class SagaStateMachineConfig extends EnumStateMachineConfigurerAdapter<Estados, Eventos> {

    private final EmpleadoSagaActions empleadoActions;
    private final ContratoSagaActions contratoActions;
    private final CompensacionSagaActions compensacionActions;

    @Autowired
    public SagaStateMachineConfig(EmpleadoSagaActions empleadoActions,
                                  ContratoSagaActions contratoActions,
                                  CompensacionSagaActions compensacionActions) {
        this.empleadoActions    = empleadoActions;
        this.contratoActions    = contratoActions;
        this.compensacionActions = compensacionActions;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<Estados, Eventos> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(stateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<Estados, Eventos> states) throws Exception {
        states
                .withStates()
                .initial(Estados.INICIO)
                .states(EnumSet.allOf(Estados.class))

                // --- Cuando entres a CREAR_EMPLEADO, ejecuta la acción de creación de empleado ---
                .stateEntry(Estados.CREAR_EMPLEADO, context -> {
                    empleadoActions.crearEmpleado(context);
                })

                // --- Cuando entres a EMPLEADO_CREADO, dispara SOLICITAR_CREAR_CONTRATO una sola vez ---
                .stateEntry(Estados.EMPLEADO_CREADO, context -> {
                    StateMachine<Estados, Eventos> machine = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder.withPayload(Eventos.SOLICITAR_CREAR_CONTRATO).build();
                    machine.sendEvent(msg);
                    log.info("[SAGA] Emitido SOLICITAR_CREAR_CONTRATO desde estado EMPLEADO_CREADO");
                })

                // --- Cuando entres a CREAR_CONTRATO, ejecuta la acción de creación de contrato ---
                .stateEntry(Estados.CREAR_CONTRATO, context -> {
                    contratoActions.crearContrato(context);
                })

                // --- Cuando entres a COMPENSAR_EMPLEADO, ejecuta la acción de compensación ---
                .stateEntry(Estados.COMPENSAR_EMPLEADO, context -> {
                    compensacionActions.compensarEmpleado(context);
                })

                // --- Estados finales (no deben tener acciones de entry) ---
                .end(Estados.EMPLEADO_EXISTE)
                .end(Estados.REVERTIDA)
                .end(Estados.CONTRATO_CREADO)   // opcionalmente podrías dejarlo “intermedio” hasta FINALIZAR
                .end(Estados.FINALIZADA)
                .end(Estados.FALLIDA);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Estados, Eventos> transitions) throws Exception {
        transitions

                // 1) INICIO → CREAR_EMPLEADO cuándo llegue SOLICITAR_CREAR_EMPLEADO
                //    (la acción de negocio “crearEmpleado” se ejecuta en stateEntry de CREAR_EMPLEADO)
                .withExternal()
                .source(Estados.INICIO)
                .target(Estados.CREAR_EMPLEADO)
                .event(Eventos.SOLICITAR_CREAR_EMPLEADO)
                .and()

                // 2) CREAR_EMPLEADO → EMPLEADO_EXISTE si llega EMPLEADO_EXISTE (duplicado de documento)
                .withExternal()
                .source(Estados.CREAR_EMPLEADO)
                .target(Estados.EMPLEADO_EXISTE)
                .event(Eventos.EMPLEADO_EXISTE)
                .and()

                // 3) CREAR_EMPLEADO → REVERTIDA si llega CB_REVERTIDO (CircuitBreaker en crearEmpleado)
                .withExternal()
                .source(Estados.CREAR_EMPLEADO)
                .target(Estados.REVERTIDA)
                .event(Eventos.CB_REVERTIDO)
                .and()

                // 4) CREAR_EMPLEADO → EMPLEADO_CREADO si llega EMPLEADO_CREADO
                .withExternal()
                .source(Estados.CREAR_EMPLEADO)
                .target(Estados.EMPLEADO_CREADO)
                .event(Eventos.EMPLEADO_CREADO)
                .and()

                // 5) EMPLEADO_CREADO → CREAR_CONTRATO si llega SOLICITAR_CREAR_CONTRATO
                .withExternal()
                .source(Estados.EMPLEADO_CREADO)
                .target(Estados.CREAR_CONTRATO)
                .event(Eventos.SOLICITAR_CREAR_CONTRATO)
                .and()

                // 6) CREAR_CONTRATO → CONTRATO_CREADO si llega CONTRATO_CREADO
                .withExternal()
                .source(Estados.CREAR_CONTRATO)
                .target(Estados.CONTRATO_CREADO)
                .event(Eventos.CONTRATO_CREADO)
                .and()

                // 7) CREAR_CONTRATO → FALLIDA si llega CONTRATO_FALLIDO (duplicado de contrato sin compensar)
                .withExternal()
                .source(Estados.CREAR_CONTRATO)
                .target(Estados.FALLIDA)
                .event(Eventos.CONTRATO_FALLIDO)
                .and()

                // 8) CREAR_CONTRATO → COMPENSAR_EMPLEADO si llega FALLBACK_CONTRATO (CircuitBreaker o error)
                .withExternal()
                .source(Estados.CREAR_CONTRATO)
                .target(Estados.COMPENSAR_EMPLEADO)
                .event(Eventos.FALLBACK_CONTRATO)
                .and()

                // 9) CONTRATO_CREADO → FINALIZADA si llega FINALIZAR
                .withExternal()
                .source(Estados.CONTRATO_CREADO)
                .target(Estados.FINALIZADA)
                .event(Eventos.FINALIZAR)
                .and()

                // 10) COMPENSAR_EMPLEADO → FALLIDA si llega COMPENSAR_EMPLEADO
                .withExternal()
                .source(Estados.COMPENSAR_EMPLEADO)
                .target(Estados.FALLIDA)
                .event(Eventos.COMPENSAR_EMPLEADO)
                .and()

                // 11) TRANSICIÓN “global” de ERROR → FALLIDA (para cualquier estado no final)
                .withExternal()
                .source(Estados.INICIO)
                .source(Estados.CREAR_EMPLEADO)
                .source(Estados.EMPLEADO_CREADO)
                .source(Estados.CREAR_CONTRATO)
                .source(Estados.CONTRATO_CREADO)
                .source(Estados.COMPENSAR_EMPLEADO)
                .source(Estados.REVERTIDA)
                .source(Estados.EMPLEADO_EXISTE)
                .target(Estados.FALLIDA)
                .event(Eventos.ERROR)
                .action(context -> compensacionActions.compensarEmpleado(context));
    }

    @Bean
    public StateMachineListener<Estados, Eventos> stateMachineListener() {
        return new StateMachineListenerAdapter<Estados, Eventos>() {
            @Override
            public void stateChanged(State<Estados, Eventos> from, State<Estados, Eventos> to) {
                String anterior = (from != null ? from.getId().name() : "NINGUNO");
                String siguiente = (to   != null ? to.getId().name()   : "NINGUNO");
                log.info("[SM] Estado cambiado: [{}] → [{}]", anterior, siguiente);
            }
            @Override
            public void transition(Transition<Estados, Eventos> transition) {
                if (transition.getSource() != null && transition.getTarget() != null) {
                    log.debug("[SM] Transición: [{} → {}] evento [{}]",
                            transition.getSource().getId(),
                            transition.getTarget().getId(),
                            transition.getTrigger() != null
                                    ? transition.getTrigger().getEvent()
                                    : "NINGUNO");
                }
            }
            @Override
            public void eventNotAccepted(org.springframework.messaging.Message<Eventos> event) {
                log.warn("[SM] Evento no aceptado: [{}]",
                        event != null ? event.getPayload() : "NINGUNO");
            }
        };
    }
}
