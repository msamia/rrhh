package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.metricas.SagaMetrics;
import ar.org.hospitalcuencaalta.servicio_orquestador.excepcion.ServicioNoDisponibleException;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationHistory;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationAction;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaOperationService;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.EmpleadoDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ar.org.hospitalcuencaalta.comunes.statemachine.EventosSM;
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
    private static final String RETRY_EMPLEADO = "empleadoRetry";

    @Autowired private EmpleadoClient empleadoClient;
    @Autowired private SagaMetrics sagaMetrics;
    @Autowired private CreationHistory creationHistory;
    @Autowired private SagaOperationService sagaOperationService;

    public static class DuplicateDocumentException extends RuntimeException {
        public DuplicateDocumentException(String msg) { super(msg); }
    }

    @CircuitBreaker(name = CB_EMPLEADO, fallbackMethod = "fallbackCrearEmpleado")
    @Retry(name = RETRY_EMPLEADO, fallbackMethod = "fallbackCrearEmpleado")
    public void crearEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("crearEmpleado", (Callable<Void>) () -> {
            EmpleadoDto empleadoDto = context.getExtendedState().get("empleadoDto", EmpleadoDto.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            // 1) Verificar existencia por documento
            try {
                empleadoClient.findByDocumento(empleadoDto.getDocumento());
                // Si no lanza NotFound, ya existía
                log.warn("[SAGA] Documento {} ya existe", empleadoDto.getDocumento());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el empleado porque el empleado ya existe");
                CreationAction action = new CreationAction(
                        "CREATE_EMPLEADO", false, "empleado ya existe", java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgExists = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_EXISTE)
                        .build();
                EventosSM.enviar(machine, msgExists);
                log.info("[SAGA] Emitido EMPLEADO_EXISTE");
                return null;
            } catch (FeignException.NotFound nf) {
                // No existía: seguimos a crear
                log.info("[SAGA] Documento {} no encontrado, creando empleado", empleadoDto.getDocumento());
            }

            // 2) Crear empleado
            try {
                EmpleadoDto creado = empleadoClient.create(empleadoDto);
                Long idGenerado = creado.getId();
                // almacenar DTO con ID para eventos finales
                context.getExtendedState().getVariables().put("empleadoDto", creado);
                log.info("[SAGA] Empleado creado con id={}", idGenerado);
                CreationAction action = new CreationAction(
                        "CREATE_EMPLEADO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());

                // 3) Guardar idEmpleado en extendedState
                context.getExtendedState().getVariables().put("idEmpleado", idGenerado);

                // 4) Emitir EMPLEADO_CREADO (usamos sendEvent en lugar de sendEvents)
                Message<Eventos> msgCreado = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_CREADO)
                        .setHeader("idEmpleado", idGenerado)
                        .build();
                EventosSM.enviar(machine, msgCreado);
                log.info("[SAGA] Emitido EMPLEADO_CREADO id={}", idGenerado);
            } catch (ServicioNoDisponibleException snd) {
                log.error("[SAGA] Servicio-empleado no disponible al crear empleado: {}", snd.getMessage());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el empleado porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "CREATE_EMPLEADO", false, snd.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                log.info("[SAGA] Emitido EMPLEADO_FALLIDO");
            } catch (FeignException fe) {
                log.error("[SAGA] Error al crear empleado: {}", fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el empleado porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "CREATE_EMPLEADO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                log.info("[SAGA] Emitido EMPLEADO_FALLIDO");
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al crear empleado", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el empleado debido a un error inesperado");
                CreationAction action = new CreationAction(
                        "CREATE_EMPLEADO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.EMPLEADO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                log.info("[SAGA] Emitido EMPLEADO_FALLIDO");
            }

            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackCrearEmpleado(StateContext<Estados, Eventos> context, Throwable throwable) {
        log.warn("[SAGA] FALLBACK crearEmpleado: {}", throwable.toString());
        sagaMetrics.record("fallbackCrearEmpleado", (Callable<Void>) () -> null);

        CreationAction action = new CreationAction(
                "CREATE_EMPLEADO", false, "fallback: " + throwable.toString(), java.time.Instant.now());
        creationHistory.add(action);
        sagaOperationService.record(context.getStateMachine(), action.step(), action.success(), action.error());

        context.getExtendedState().getVariables().put(
                "mensajeError",
                "No se puede crear el empleado porque el servicio no está disponible");

        StateMachine<Estados, Eventos> machine = context.getStateMachine();
        Message<Eventos> msgFb = MessageBuilder
                .withPayload(Eventos.EMPLEADO_FALLIDO)
                .build();
        EventosSM.enviar(machine, msgFb);
        log.info("[SAGA] Emitido EMPLEADO_FALLIDO");
    }

    /** Actualiza un empleado existente y emite EMPLEADO_ACTUALIZADO. */
    @Retry(name = RETRY_EMPLEADO)
    public void actualizarEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("actualizarEmpleado", (Callable<Void>) () -> {
            EmpleadoDto dto = context.getExtendedState().get("empleadoDto", EmpleadoDto.class);
            Long id = context.getExtendedState().get("idEmpleado", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            try {
                EmpleadoDto actualizado = empleadoClient.update(id, dto);
                // mantener DTO actualizado
                context.getExtendedState().getVariables().put("empleadoDto", actualizado);

                CreationAction action = new CreationAction(
                        "UPDATE_EMPLEADO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());

                Message<Eventos> msg = MessageBuilder.withPayload(Eventos.EMPLEADO_ACTUALIZADO)
                        .setHeader("idEmpleado", id)
                        .build();
                EventosSM.enviar(machine, msg);
                log.info("[SAGA] Emitido EMPLEADO_ACTUALIZADO id={}", id);
            } catch (FeignException fe) {
                log.error("[SAGA] Error al actualizar empleado id={}: {}", id, fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede actualizar el empleado porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "UPDATE_EMPLEADO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.EMPLEADO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al actualizar empleado", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede actualizar el empleado debido a un error inesperado");
                CreationAction action = new CreationAction(
                        "UPDATE_EMPLEADO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.EMPLEADO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            }

            return null;
        });
    }

    /** Elimina un empleado existente y emite EMPLEADO_ELIMINADO. */
    @Retry(name = RETRY_EMPLEADO)
    public void eliminarEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("eliminarEmpleado", (Callable<Void>) () -> {
            Long id = context.getExtendedState().get("idEmpleado", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            try {
                empleadoClient.delete(id);

                Message<Eventos> msg = MessageBuilder.withPayload(Eventos.EMPLEADO_ELIMINADO)
                        .setHeader("idEmpleado", id)
                        .build();
                EventosSM.enviar(machine, msg);
                log.info("[SAGA] Emitido EMPLEADO_ELIMINADO id={}", id);
                CreationAction action = new CreationAction(
                        "DELETE_EMPLEADO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
            } catch (FeignException fe) {
                log.error("[SAGA] Error al eliminar empleado id={}: {}", id, fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede eliminar el empleado porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "DELETE_EMPLEADO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.EMPLEADO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al eliminar empleado", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede eliminar el empleado debido a un error inesperado");
                CreationAction action = new CreationAction(
                        "DELETE_EMPLEADO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.EMPLEADO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            }

            return null;
        });
    }
}
