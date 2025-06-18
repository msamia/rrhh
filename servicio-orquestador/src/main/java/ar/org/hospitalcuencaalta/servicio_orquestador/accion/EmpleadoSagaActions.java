package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.metricas.SagaMetrics;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Slf4j
@Component
public class EmpleadoSagaActions {

    private static final String CB_EMPLEADO = "crearEmpleadoCB";

    @Autowired private EmpleadoClient empleadoClient;
    @Autowired private SagaMetrics sagaMetrics;

    public static class DuplicateDocumentException extends RuntimeException {
        public DuplicateDocumentException(String msg) { super(msg); }
    }

    @CircuitBreaker(name = CB_EMPLEADO, fallbackMethod = "fallbackCrearEmpleado")
    public void crearEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("crearEmpleado", (Callable<Void>) () -> {
            EmpleadoDto empleadoDto = context.getExtendedState().get("empleadoDto", EmpleadoDto.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            // 1) Verificar existencia por documento
            try {
                empleadoClient.findByDocumento(empleadoDto.getDocumento());
                // Si no lanza NotFound, ya existía
                log.warn("[SAGA] Documento {} ya existe", empleadoDto.getDocumento());
                Message<Eventos> msgExists = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_EXISTE)
                        .build();
                machine.sendEvent(msgExists);
                log.info("[SAGA] Emitido EMPLEADO_EXISTE");
                return null;
            } catch (FeignException.NotFound nf) {
                // No existía: seguimos a crear
                log.info("[SAGA] Documento {} no encontrado, creando empleado", empleadoDto.getDocumento());
            }

            // 2) Crear empleado
            EmpleadoDto creado = empleadoClient.create(empleadoDto);
            Long idGenerado = creado.getId();
            // almacenar DTO con ID para eventos finales
            context.getExtendedState().getVariables().put("empleadoDto", creado);
            log.info("[SAGA] Empleado creado con id={}", idGenerado);

            // 3) Guardar idEmpleado en extendedState
            context.getExtendedState().getVariables().put("idEmpleado", idGenerado);

            // 4) Emitir EMPLEADO_CREADO (usamos sendEvent en lugar de sendEvents)
            Message<Eventos> msgCreado = MessageBuilder
                    .withPayload(Eventos.EMPLEADO_CREADO)
                    .setHeader("idEmpleado", idGenerado)
                    .build();
            machine.sendEvent(msgCreado);
            log.info("[SAGA] Emitido EMPLEADO_CREADO id={}", idGenerado);

            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackCrearEmpleado(StateContext<Estados, Eventos> context, Throwable throwable) {
        log.warn("[SAGA] FALLBACK crearEmpleado: {}", throwable.toString());
        sagaMetrics.record("fallbackCrearEmpleado", (Callable<Void>) () -> null);

        StateMachine<Estados, Eventos> machine = context.getStateMachine();
        Message<Eventos> msgFb = MessageBuilder
                .withPayload(Eventos.FALLBACK_EMPLEADO)
                .build();
        machine.sendEvent(msgFb);
        log.info("[SAGA] Emitido FALLBACK_EMPLEADO");
    }

    /** Actualiza un empleado existente y emite EMPLEADO_ACTUALIZADO. */
    public void actualizarEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("actualizarEmpleado", (Callable<Void>) () -> {
            EmpleadoDto dto = context.getExtendedState().get("empleadoDto", EmpleadoDto.class);
            Long id = context.getExtendedState().get("idEmpleado", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            EmpleadoDto actualizado = empleadoClient.update(id, dto);
            // mantener DTO actualizado
            context.getExtendedState().getVariables().put("empleadoDto", actualizado);

            Message<Eventos> msg = MessageBuilder.withPayload(Eventos.EMPLEADO_ACTUALIZADO)
                    .setHeader("idEmpleado", id)
                    .build();
            machine.sendEvent(msg);
            log.info("[SAGA] Emitido EMPLEADO_ACTUALIZADO id={}", id);
            return null;
        });
    }

    /** Elimina un empleado existente y emite EMPLEADO_ELIMINADO. */
    public void eliminarEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("eliminarEmpleado", (Callable<Void>) () -> {
            Long id = context.getExtendedState().get("idEmpleado", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            empleadoClient.delete(id);

            Message<Eventos> msg = MessageBuilder.withPayload(Eventos.EMPLEADO_ELIMINADO)
                    .setHeader("idEmpleado", id)
                    .build();
            machine.sendEvent(msg);
            log.info("[SAGA] Emitido EMPLEADO_ELIMINADO id={}", id);
            return null;
        });
    }
}
