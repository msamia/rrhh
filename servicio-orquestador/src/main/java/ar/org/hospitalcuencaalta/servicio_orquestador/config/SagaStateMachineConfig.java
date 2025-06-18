package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import ar.org.hospitalcuencaalta.servicio_orquestador.accion.CompensacionSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.ContratoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.EmpleadoSagaActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.accion.SagaCompletionActions;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaStateService;
import lombok.extern.slf4j.Slf4j;
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
import reactor.core.publisher.Mono;

import java.util.EnumSet;

@Slf4j
@Configuration
@EnableStateMachineFactory
public class SagaStateMachineConfig
        extends EnumStateMachineConfigurerAdapter<Estados, Eventos> {

    private final EmpleadoSagaActions empleadoActions;
    private final ContratoSagaActions contratoActions;
    private final CompensacionSagaActions compensacionActions;
    private final SagaCompletionActions completionActions;
    private final SagaStateService sagaStateService;

    public SagaStateMachineConfig(EmpleadoSagaActions empleadoActions,
                                  ContratoSagaActions contratoActions,
                                  CompensacionSagaActions compensacionActions,
                                  SagaCompletionActions completionActions,
                                  SagaStateService sagaStateService) {
        this.empleadoActions     = empleadoActions;
        this.contratoActions     = contratoActions;
        this.compensacionActions = compensacionActions;
        this.completionActions   = completionActions;
        this.sagaStateService    = sagaStateService;
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<Estados, Eventos> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(stateMachineListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<Estados, Eventos> states) throws Exception {
        states
                .withStates()
                .initial(Estados.INICIO)
                .states(EnumSet.allOf(Estados.class))
                .end(Estados.EMPLEADO_EXISTE)
                .end(Estados.REVERTIDA)
                .end(Estados.FALLIDA)
                .end(Estados.FINALIZADA)

                // ⑤ Al entrar en CREAR_EMPLEADO
                .stateEntry(Estados.CREAR_EMPLEADO, context -> {
                    empleadoActions.crearEmpleado(context);
                    sagaStateService.save(context.getStateMachine());
                })

                // ⑥ Al entrar en EMPLEADO_CREADO disparamos SOLICITAR_CREAR_CONTRATO
                .stateEntry(Estados.EMPLEADO_CREADO, context -> {
                    StateMachine<Estados, Eventos> machine = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.SOLICITAR_CREAR_CONTRATO)
                            .build();
                    // En 4.0 sendEvent(E) está deprecado; usamos la variante reactiva
                    machine.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(machine);
                    log.info("[SAGA] {} enviado", Eventos.SOLICITAR_CREAR_CONTRATO);
                })

                // ⑦ Al entrar en CREAR_CONTRATO
                .stateEntry(Estados.CREAR_CONTRATO, context -> {
                    contratoActions.crearContrato(context);
                    sagaStateService.save(context.getStateMachine());
                })

                // --- Actualizaciones ---
                .stateEntry(Estados.ACTUALIZAR_EMPLEADO, context -> {
                    empleadoActions.actualizarEmpleado(context);
                    sagaStateService.save(context.getStateMachine());
                })

                .stateEntry(Estados.EMPLEADO_ACTUALIZADO, context -> {
                    StateMachine<Estados, Eventos> machine = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.SOLICITAR_ACTUALIZAR_CONTRATO)
                            .build();
                    machine.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(machine);
                    log.info("[SAGA] {} enviado", Eventos.SOLICITAR_ACTUALIZAR_CONTRATO);
                })

                .stateEntry(Estados.ACTUALIZAR_CONTRATO, context -> {
                    contratoActions.actualizarContrato(context);
                    sagaStateService.save(context.getStateMachine());
                })

                .stateEntry(Estados.CONTRATO_ACTUALIZADO, context -> {
                    StateMachine<Estados, Eventos> sm = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.FINALIZAR)
                            .build();
                    sm.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(sm);
                    log.info("[SAGA] {} enviado", Eventos.FINALIZAR);
                })

                // --- Eliminaciones ---
                .stateEntry(Estados.ELIMINAR_CONTRATO, context -> {
                    contratoActions.eliminarContrato(context);
                    sagaStateService.save(context.getStateMachine());
                })

                .stateEntry(Estados.CONTRATO_ELIMINADO, context -> {
                    StateMachine<Estados, Eventos> machine = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.SOLICITAR_ELIMINAR_EMPLEADO)
                            .build();
                    machine.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(machine);
                    log.info("[SAGA] {} enviado", Eventos.SOLICITAR_ELIMINAR_EMPLEADO);
                })

                .stateEntry(Estados.ELIMINAR_EMPLEADO, context -> {
                    empleadoActions.eliminarEmpleado(context);
                    sagaStateService.save(context.getStateMachine());
                })

                .stateEntry(Estados.EMPLEADO_ELIMINADO, context -> {
                    StateMachine<Estados, Eventos> sm = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.FINALIZAR)
                            .build();
                    sm.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(sm);
                    log.info("[SAGA] {} enviado", Eventos.FINALIZAR);
                })

                // ⑧ Al entrar en CONTRATO_CREADO enviamos FINALIZAR
                .stateEntry(Estados.CONTRATO_CREADO, context -> {
                    StateMachine<Estados, Eventos> sm = context.getStateMachine();
                    Message<Eventos> msg = MessageBuilder
                            .withPayload(Eventos.FINALIZAR)
                            .build();
                    sm.sendEvent(Mono.just(msg)).subscribe();
                    sagaStateService.save(sm);
                    log.info("[SAGA] {} enviado", Eventos.FINALIZAR);
                })

                // ⑨ Al entrar en COMPENSAR_EMPLEADO
                .stateEntry(Estados.COMPENSAR_EMPLEADO, context -> {
                    compensacionActions.compensarEmpleado(context);
                    sagaStateService.save(context.getStateMachine());
                })

                // ⑩ Al entrar en FINALIZADA
                .stateEntry(Estados.FINALIZADA, context -> {
                    completionActions.onSagaCompleted(context);
                    sagaStateService.save(context.getStateMachine());
                    log.info("[SAGA] Finalizada y eventos publicados");
                });
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Estados, Eventos> transitions) throws Exception {
        transitions
                // 1) INICIO → CREAR_EMPLEADO
                .withExternal()
                .source(Estados.INICIO).target(Estados.CREAR_EMPLEADO)
                .event(Eventos.SOLICITAR_CREAR_EMPLEADO)

                .and()
                // INICIO → ACTUALIZAR_EMPLEADO
                .withExternal()
                .source(Estados.INICIO).target(Estados.ACTUALIZAR_EMPLEADO)
                .event(Eventos.SOLICITAR_ACTUALIZAR_EMPLEADO)

                .and()
                // INICIO → ELIMINAR_CONTRATO
                .withExternal()
                .source(Estados.INICIO).target(Estados.ELIMINAR_CONTRATO)
                .event(Eventos.SOLICITAR_ELIMINAR_CONTRATO)

                .and()
                // 2) CREAR_EMPLEADO → EMPLEADO_CREADO
                .withExternal()
                .source(Estados.CREAR_EMPLEADO).target(Estados.EMPLEADO_CREADO)
                .event(Eventos.EMPLEADO_CREADO)

                .and()
                // 3) EMPLEADO_CREADO → CREAR_CONTRATO
                .withExternal()
                .source(Estados.EMPLEADO_CREADO).target(Estados.CREAR_CONTRATO)
                .event(Eventos.SOLICITAR_CREAR_CONTRATO)

                .and()
                // 4) CREAR_CONTRATO → CONTRATO_CREADO
                .withExternal()
                .source(Estados.CREAR_CONTRATO).target(Estados.CONTRATO_CREADO)
                .event(Eventos.CONTRATO_CREADO)

                .and()
                // 5) CONTRATO_CREADO → FINALIZADA
                .withExternal()
                .source(Estados.CONTRATO_CREADO).target(Estados.FINALIZADA)
                .event(Eventos.FINALIZAR)

                // --- Transiciones de actualización ---
                .and()
                .withExternal()
                .source(Estados.ACTUALIZAR_EMPLEADO).target(Estados.EMPLEADO_ACTUALIZADO)
                .event(Eventos.EMPLEADO_ACTUALIZADO)

                .and()
                .withExternal()
                .source(Estados.EMPLEADO_ACTUALIZADO).target(Estados.ACTUALIZAR_CONTRATO)
                .event(Eventos.SOLICITAR_ACTUALIZAR_CONTRATO)

                .and()
                .withExternal()
                .source(Estados.ACTUALIZAR_CONTRATO).target(Estados.CONTRATO_ACTUALIZADO)
                .event(Eventos.CONTRATO_ACTUALIZADO)

                .and()
                .withExternal()
                .source(Estados.CONTRATO_ACTUALIZADO).target(Estados.FINALIZADA)
                .event(Eventos.FINALIZAR)

                // --- Transiciones de eliminación ---
                .and()
                .withExternal()
                .source(Estados.ELIMINAR_CONTRATO).target(Estados.CONTRATO_ELIMINADO)
                .event(Eventos.CONTRATO_ELIMINADO)

                .and()
                .withExternal()
                .source(Estados.CONTRATO_ELIMINADO).target(Estados.ELIMINAR_EMPLEADO)
                .event(Eventos.SOLICITAR_ELIMINAR_EMPLEADO)

                .and()
                .withExternal()
                .source(Estados.ELIMINAR_EMPLEADO).target(Estados.EMPLEADO_ELIMINADO)
                .event(Eventos.EMPLEADO_ELIMINADO)

                .and()
                .withExternal()
                .source(Estados.EMPLEADO_ELIMINADO).target(Estados.FINALIZADA)
                .event(Eventos.FINALIZAR)

                // Fallbacks y errores
                .and()
                .withExternal()
                .source(Estados.CREAR_EMPLEADO).target(Estados.EMPLEADO_EXISTE)
                .event(Eventos.EMPLEADO_EXISTE)

                .and()
                .withExternal()
                .source(Estados.CREAR_EMPLEADO).target(Estados.COMPENSAR_EMPLEADO)
                .event(Eventos.FALLBACK_EMPLEADO)

                .and()
                .withExternal()
                .source(Estados.CREAR_CONTRATO).target(Estados.FALLIDA)
                .event(Eventos.CONTRATO_FALLIDO)

                .and()
                .withExternal()
                .source(Estados.CREAR_CONTRATO).target(Estados.REVERTIDA)
                .event(Eventos.FALLBACK_CONTRATO)

                .and()
                .withExternal()
                .source(Estados.COMPENSAR_EMPLEADO).target(Estados.REVERTIDA)
                .event(Eventos.COMPENSAR_EMPLEADO);
    }

    /** ⑪ Listener para trazas */
    @Bean
    public StateMachineListener<Estados, Eventos> stateMachineListener() {
        return new StateMachineListenerAdapter<Estados, Eventos>() {
            @Override
            public void stateChanged(State<Estados, Eventos> from, State<Estados, Eventos> to) {
                log.debug("[SAGA] Estado: {} → {}",
                        from == null ? "none" : from.getId(), to.getId());
            }
            @Override
            public void transition(Transition<Estados, Eventos> transition) {
                if (transition != null && transition.getTrigger() != null) {
                    log.debug("[SAGA] Evento {}: {} → {}",
                            transition.getTrigger().getEvent(),
                            transition.getSource().getId(),
                            transition.getTarget().getId());
                }
            }
        };
    }
}
