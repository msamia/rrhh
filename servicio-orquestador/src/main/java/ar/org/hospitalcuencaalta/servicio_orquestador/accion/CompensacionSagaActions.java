package ar.org.hospitalcuencaalta.servicio_orquestador.accion;

import ar.org.hospitalcuencaalta.servicio_orquestador.feign.EmpleadoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.feign.ContratoClient;
import ar.org.hospitalcuencaalta.servicio_orquestador.metricas.SagaMetrics;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Eventos;
import ar.org.hospitalcuencaalta.servicio_orquestador.modelo.Estados;
import ar.org.hospitalcuencaalta.servicio_orquestador.web.dto.CompensacionDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import ar.org.hospitalcuencaalta.comunes.statemachine.EventosSM;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@Slf4j
@Component
public class CompensacionSagaActions {

    private static final String CB_COMPENSACION = "compensarEmpleadoCB";

    @Autowired private EmpleadoClient empleadoClient;
    @Autowired private ContratoClient contratoClient;
    @Autowired private KafkaTemplate<String, CompensacionDto> kafkaTemplate;
    @Autowired private SagaMetrics sagaMetrics;

    @CircuitBreaker(name = CB_COMPENSACION, fallbackMethod = "fallbackCompensacion")
    public void compensarEmpleado(StateContext<Estados, Eventos> context) {
        sagaMetrics.record("compensarEmpleado", (Callable<Void>) () -> {
            StateMachine<Estados, Eventos> machine = context.getStateMachine();

            // 1) Borrar contrato si existe
            Long idContrato = context.getExtendedState().get("idContrato", Long.class);
            if (idContrato != null) {
                try {
                    log.info("[Compensación] Borrando contrato id={}", idContrato);
                    contratoClient.delete(idContrato);
                    log.info("[Compensación] Contrato id={} borrado", idContrato);
                } catch (Exception e) {
                    log.error("[Compensación] Error borrando contrato id={}: {}", idContrato, e.toString());
                }
            }

            // 2) Borrar empleado si existe
            Long idEmpleado = context.getExtendedState().get("idEmpleado", Long.class);
            if (idEmpleado != null) {
                try {
                    log.info("[Compensación] Borrando empleado id={}", idEmpleado);
                    empleadoClient.delete(idEmpleado);
                    log.info("[Compensación] Empleado id={} borrado", idEmpleado);
                } catch (Exception e) {
                    log.error("[Compensación] Error borrando empleado id={}: {}", idEmpleado, e.toString());
                }
            }

            // 3) Publicar evento en Kafka si existe CompensacionDto
            CompensacionDto compDto = context.getExtendedState().get("compensacionDto", CompensacionDto.class);
            if (compDto != null) {
                compDto.setFechaCompensacion(LocalDateTime.now());
                log.info("[Compensación] Publicando en Kafka: {}", compDto);
                kafkaTemplate.send("saga.compensated", compDto);
            }

            // 4) Emitir COMPENSAR_EMPLEADO
            EventosSM.enviar(machine, Eventos.COMPENSAR_EMPLEADO);
            log.info("[SAGA] Emitido COMPENSAR_EMPLEADO");
            return null;
        });
    }

    @SuppressWarnings("unused")
    public void fallbackCompensacion(StateContext<Estados, Eventos> context, Throwable ex) {
        log.error("[Compensación] FALLBACK compensarEmpleado: {}", ex.toString());
        sagaMetrics.record("fallbackCompensacion", (Callable<Void>) () -> null);
        context.getStateMachine().stopReactively().subscribe();
        log.info("[SAGA] Máquina detenida tras fallo en compensación");
    }
}
