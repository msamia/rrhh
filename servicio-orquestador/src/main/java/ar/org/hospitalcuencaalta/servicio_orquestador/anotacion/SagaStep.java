package ar.org.hospitalcuencaalta.servicio_orquestador.anotacion;

import java.lang.annotation.*;

/**
 * Anotación para marcar cada método de la SAGA y especificar el nombre del
 * CircuitBreaker (y demás políticas) que se utilizará.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SagaStep {
    /**
     * Clave con la que se registraron las políticas (CircuitBreaker,
     * RateLimiter, TimeLimiter y Bulkhead) en Resilience4j.
     */
    String breaker();
}
