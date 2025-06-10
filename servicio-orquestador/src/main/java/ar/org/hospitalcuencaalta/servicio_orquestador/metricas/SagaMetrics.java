package ar.org.hospitalcuencaalta.servicio_orquestador.metricas;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Métricas genéricas para SAGA: record mide el tiempo y atrapa errores en las métricas
 * en lugar de interrumpir la SAGA. Cualquier excepción se registra en el log.
 */
@Component
public class SagaMetrics {

    private static final Logger log = LoggerFactory.getLogger(SagaMetrics.class);
    private final MeterRegistry registry;

    public SagaMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    /**
     * Registra duración y contador genérico para un paso de SAGA sin interrumpir
     * la lógica si la métrica falla.
     *
     * @param name Identificador de la operación ("fallbackCrearEmpleado", etc.)
     * @param task Callable que realiza la operación (puede lanzar excepción)
     * @return El resultado de task.call(), o null si task lanzó excepción.
     */
    public <T> T record(String name, Callable<T> task) {
        long start = System.currentTimeMillis();
        try {
            return task.call();
        } catch (Exception ex) {
            // Atrapar excepciones de task.call() o de la métrica
            log.warn("[SagaMetrics] Excepción en métricas para paso '{}': {}", name, ex.toString());
            return null;
        } finally {
            long duration = System.currentTimeMillis() - start;
            try {
                registry.timer("saga.timer.generic", "step", name)
                        .record(duration, TimeUnit.MILLISECONDS);
            } catch (Exception ex) {
                // Si falla al grabar el timer, solo lo logueamos
                log.warn("[SagaMetrics] No se pudo registrar timer '{}' : {}", name, ex.toString());
            }
        }
    }
}
