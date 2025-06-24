package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.feign.ContratoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.metricas.SagaMetrics;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationHistory;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationAction;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.servicio.SagaOperationService;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.CompensacionDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
public class ContratoSagaActions {

    private static final String CB_CONTRATO = "crearContratoCB";

    @Autowired private ContratoClient contratoClient;
    @Autowired private SagaMetrics sagaMetrics;
    @Autowired private CreationHistory creationHistory;
    @Autowired private SagaOperationService sagaOperationService;

    public static class ContractConflictException extends RuntimeException {
        public ContractConflictException(String msg) { super(msg); }
    }

    @CircuitBreaker(name = CB_CONTRATO, fallbackMethod = "fallbackCrearContrato")
    public void crearContrato(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("crearContrato", (Callable<Void>) () -> {
            ContratoLaboralDto contratoDto = context.getExtendedState().get("contratoDto", ContratoLaboralDto.class);
            Long idEmpleado = context.getExtendedState().get("idEmpleado", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            log.info("[SAGA] Intentando crear contrato para empleadoId={}", idEmpleado);
            contratoDto.setEmpleadoId(idEmpleado);
            if (contratoDto.getRegimen() == null) {
                contratoDto.setRegimen("general");
            }

            try {
                // Crear contrato
                ContratoLaboralDto creado = contratoClient.create(contratoDto);
                Long idContrato = creado.getId();
                // Guardar DTO completo para eventos finales
                context.getExtendedState().getVariables().put("contratoDto", creado);
                log.info("[SAGA] Contrato creado con id={}", idContrato);
                CreationAction action = new CreationAction(
                        "CREATE_CONTRATO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());

                // Guardar idContrato
                context.getExtendedState().getVariables().put("idContrato", idContrato);

                // Emitir CONTRATO_CREADO
                Message<Eventos> msgCreado = MessageBuilder
                        .withPayload(Eventos.CONTRATO_CREADO)
                        .setHeader("idContrato", idContrato)
                        .setHeader("idEmpleado", idEmpleado)
                        .build();
                EventosSM.enviar(machine, msgCreado);
                log.info("[SAGA] Emitido CONTRATO_CREADO id={}", idContrato);
            } catch (FeignException.BadRequest bad) {
                log.warn("[SAGA] Datos inválidos al crear contrato para empleadoId={}, {}", idEmpleado, bad.contentUTF8());
                context.getExtendedState().getVariables()
                        .put("mensajeError", "No se puede crear el contrato. Campos obligatorios faltantes");
                CreationAction action = new CreationAction(
                        "CREATE_CONTRATO", false, bad.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.CONTRATO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
            } catch (FeignException.Conflict conflict) {
                log.warn("[SAGA] Contrato duplicado para empleadoId={}", idEmpleado);
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.CONTRATO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                context.getExtendedState().getVariables()
                        .put("mensajeError", "No se puede crear el contrato porque el contrato ya existe");
                CreationAction action = new CreationAction(
                        "CREATE_CONTRATO", false, "contrato duplicado", java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
            } catch (FeignException fe) {
                log.error("[SAGA] Error al crear contrato: {}", fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el contrato porque el servicio no está disponible");
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.CONTRATO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                CreationAction action = new CreationAction(
                        "CREATE_CONTRATO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al crear contrato", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede crear el contrato debido a un error inesperado");
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.CONTRATO_FALLIDO)
                        .build();
                EventosSM.enviar(machine, msgErr);
                CreationAction action = new CreationAction(
                        "CREATE_CONTRATO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
            }

            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackCrearContrato(StateContext<Estados, Eventos> context, Throwable throwable) {
        Long idEmpleado = context.getExtendedState().get("idEmpleado", Long.class);
        log.warn("[SAGA] FALLBACK crearContrato para empleadoId={}, causa={}", idEmpleado, throwable.toString());

        sagaMetrics.record("fallbackCrearContrato", (Callable<Void>) () -> null);

        CreationAction action = new CreationAction(
                "CREATE_CONTRATO", false, "fallback: " + throwable.toString(), java.time.Instant.now());
        creationHistory.add(action);
        sagaOperationService.record(context.getStateMachine(), action.step(), action.success(), action.error());

        context.getExtendedState().getVariables().put(
                "mensajeError",
                "No se puede crear el contrato porque el servicio no está disponible");

        if (context.getExtendedState().get("compensacionDto", CompensacionDto.class) == null) {
            CompensacionDto compDto = CompensacionDto.builder()
                    .empleadoId(idEmpleado)
                    .motivo("Error CB en crearContrato: " + throwable.getMessage())
                    .build();
            context.getExtendedState().getVariables().put("compensacionDto", compDto);
        }

        StateMachine<Estados, Eventos> machine = context.getStateMachine();
        Message<Eventos> msgFb = MessageBuilder
                .withPayload(Eventos.FALLBACK_CONTRATO)
                .build();
        EventosSM.enviar(machine, msgFb);
        log.info("[SAGA] Emitido FALLBACK_CONTRATO");
    }

    /**
     * Actualiza un contrato existente y emite CONTRATO_ACTUALIZADO
     * (la máquina de estados ahora pasa directo a FINALIZADA).
     * Si ocurre un error o se dispara el circuit breaker, se envía
     * FALLBACK_CONTRATO para disparar la compensación correspondiente.
     */
    @CircuitBreaker(name = CB_CONTRATO, fallbackMethod = "fallbackActualizarContrato")
    public void actualizarContrato(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("actualizarContrato", (Callable<Void>) () -> {
            Long idContrato = context.getExtendedState().get("idContrato", Long.class);
            ContratoLaboralDto dto = context.getExtendedState().get("contratoDto", ContratoLaboralDto.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            try {
                ContratoLaboralDto actualizado = contratoClient.update(idContrato, dto);
                context.getExtendedState().getVariables().put("contratoDto", actualizado);

                CreationAction action = new CreationAction(
                        "UPDATE_CONTRATO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());

                Message<Eventos> msg = MessageBuilder.withPayload(Eventos.CONTRATO_ACTUALIZADO)
                        .setHeader("idContrato", idContrato)
                        .build();
                EventosSM.enviar(machine, msg);
                log.info("[SAGA] Emitido CONTRATO_ACTUALIZADO id={}", idContrato);
            } catch (FeignException.BadRequest bad) {
                log.warn("[SAGA] Datos inválidos al actualizar contrato id={}, {}", idContrato, bad.contentUTF8());
                context.getExtendedState().getVariables()
                        .put("mensajeError", "No se puede actualizar el contrato. Campos obligatorios faltantes");
                CreationAction action = new CreationAction(
                        "UPDATE_CONTRATO", false, bad.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.CONTRATO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            } catch (FeignException fe) {
                log.error("[SAGA] Error al actualizar contrato id={}: {}", idContrato, fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede actualizar el contrato porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "UPDATE_CONTRATO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.CONTRATO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al actualizar contrato", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede actualizar el contrato debido a un error inesperado");
                CreationAction action = new CreationAction(
                        "UPDATE_CONTRATO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.CONTRATO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            }

            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackActualizarContrato(StateContext<Estados, Eventos> context, Throwable throwable) {
        Long idContrato = context.getExtendedState().get("idContrato", Long.class);
        log.warn("[SAGA] FALLBACK actualizarContrato para contratoId={}, causa={}", idContrato, throwable.toString());

        sagaMetrics.record("fallbackActualizarContrato", (Callable<Void>) () -> null);

        CreationAction action = new CreationAction(
                "UPDATE_CONTRATO", false, "fallback: " + throwable.toString(), java.time.Instant.now());
        creationHistory.add(action);
        sagaOperationService.record(context.getStateMachine(), action.step(), action.success(), action.error());

        context.getExtendedState().getVariables().put(
                "mensajeError",
                "No se puede actualizar el contrato porque el servicio no está disponible");

        if (context.getExtendedState().get("compensacionDto", CompensacionDto.class) == null) {
            Long idEmpleado = context.getExtendedState().get("idEmpleado", Long.class);
            CompensacionDto compDto = CompensacionDto.builder()
                    .empleadoId(idEmpleado)
                    .motivo("Error CB en actualizarContrato: " + throwable.getMessage())
                    .build();
            context.getExtendedState().getVariables().put("compensacionDto", compDto);
        }

        StateMachine<Estados, Eventos> machine = context.getStateMachine();
        Message<Eventos> msgFb = MessageBuilder.withPayload(Eventos.FALLBACK_CONTRATO).build();
        EventosSM.enviar(machine, msgFb);
        log.info("[SAGA] Emitido FALLBACK_CONTRATO");
    }

    /** Elimina un contrato existente y emite CONTRATO_ELIMINADO. */
    public void eliminarContrato(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("eliminarContrato", (Callable<Void>) () -> {
            Long idContrato = context.getExtendedState().get("idContrato", Long.class);
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            try {
                contratoClient.delete(idContrato);

                Message<Eventos> msg = MessageBuilder.withPayload(Eventos.CONTRATO_ELIMINADO)
                        .setHeader("idContrato", idContrato)
                        .build();
                EventosSM.enviar(machine, msg);
                log.info("[SAGA] Emitido CONTRATO_ELIMINADO id={}", idContrato);
                CreationAction action = new CreationAction(
                        "DELETE_CONTRATO", true, null, java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
            } catch (FeignException fe) {
                log.error("[SAGA] Error al eliminar contrato id={}: {}", idContrato, fe.contentUTF8());
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede eliminar el contrato porque el servicio no está disponible");
                CreationAction action = new CreationAction(
                        "DELETE_CONTRATO", false, fe.contentUTF8(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.CONTRATO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            } catch (Exception ex) {
                log.error("[SAGA] Error inesperado al eliminar contrato", ex);
                context.getExtendedState().getVariables().put(
                        "mensajeError",
                        "No se puede eliminar el contrato debido a un error inesperado");
                CreationAction action = new CreationAction(
                        "DELETE_CONTRATO", false, ex.toString(), java.time.Instant.now());
                creationHistory.add(action);
                sagaOperationService.record(machine, action.step(), action.success(), action.error());
                Message<Eventos> msgErr = MessageBuilder.withPayload(Eventos.CONTRATO_FALLIDO).build();
                EventosSM.enviar(machine, msgErr);
            }

            return null;
        });
    }

    public void eliminarContrato(Long idContrato) {
        try {
            log.info("[Compensación] Borrando contrato id={}", idContrato);
            contratoClient.delete(idContrato);
            log.info("[Compensación] Contrato id={} borrado", idContrato);
            CreationAction action = new CreationAction(
                    "COMPENSATE_DELETE_CONTRATO", true, null, java.time.Instant.now());
            creationHistory.add(action);
            sagaOperationService.recordBySagaId(null, action.step(), action.success(), action.error());
        } catch (Exception e) {
            log.error("[Compensación] Error borrando contrato id={}: {}", idContrato, e.toString());
            CreationAction action = new CreationAction(
                    "COMPENSATE_DELETE_CONTRATO", false, e.toString(), java.time.Instant.now());
            creationHistory.add(action);
            sagaOperationService.recordBySagaId(null, action.step(), action.success(), action.error());
        }
    }
}
