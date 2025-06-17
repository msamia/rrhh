package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.feign.ContratoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.metricas.SagaMetrics;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.CompensacionDto;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.ContratoLaboralDto;
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
public class ContratoSagaActions {

    private static final String CB_CONTRATO = "crearContratoCB";

    @Autowired private ContratoClient contratoClient;
    @Autowired private SagaMetrics sagaMetrics;

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
                log.info("[SAGA] Contrato creado con id={}", idContrato);

                // Guardar idContrato
                context.getExtendedState().getVariables().put("idContrato", idContrato);

                // Emitir CONTRATO_CREADO
                Message<Eventos> msgCreado = MessageBuilder
                        .withPayload(Eventos.CONTRATO_CREADO)
                        .setHeader("idContrato", idContrato)
                        .setHeader("idEmpleado", idEmpleado)
                        .build();
                machine.sendEvent(msgCreado);
                log.info("[SAGA] Emitido CONTRATO_CREADO id={}", idContrato);
            } catch (FeignException.BadRequest bad) {
                log.warn("[SAGA] Datos inválidos al crear contrato para empleadoId={}, {}", idEmpleado, bad.contentUTF8());
                context.getExtendedState().getVariables()
                        .put("mensajeError", "Campos obligatorios faltantes");
                Message<Eventos> msgErr = MessageBuilder
                        .withPayload(Eventos.CONTRATO_FALLIDO)
                        .build();
                machine.sendEvent(msgErr);
            } catch (FeignException.Conflict conflict) {
                log.warn("[SAGA] Contrato duplicado para empleadoId={}", idEmpleado);
                throw new ContractConflictException("Contrato duplicado");
            }

            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackCrearContrato(StateContext<Estados, Eventos> context, Throwable throwable) {
        Long idEmpleado = context.getExtendedState().get("idEmpleado", Long.class);
        log.warn("[SAGA] FALLBACK crearContrato para empleadoId={}, causa={}", idEmpleado, throwable.toString());

        sagaMetrics.record("fallbackCrearContrato", (Callable<Void>) () -> null);

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
        machine.sendEvent(msgFb);
        log.info("[SAGA] Emitido FALLBACK_CONTRATO");
    }

    public void eliminarContrato(Long idContrato) {
        try {
            log.info("[Compensación] Borrando contrato id={}", idContrato);
            contratoClient.delete(idContrato);
            log.info("[Compensación] Contrato id={} borrado", idContrato);
        } catch (Exception e) {
            log.error("[Compensación] Error borrando contrato id={}: {}", idContrato, e.toString());
        }
    }
}
