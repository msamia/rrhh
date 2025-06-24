package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.monitoring.endpoint.BulkheadEndpoint;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpoint;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.monitoring.endpoint.RateLimiterEndpoint;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.monitoring.endpoint.RetryEndpoint;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.timelimiter.monitoring.endpoint.TimeLimiterEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers Resilience4j actuator endpoints when auto-configuration is not
 * activated. These beans expose circuit breakers, rate limiters, bulkheads and
 * other resilience components through Spring Boot Actuator.
 */
@Configuration
public class Resilience4jEndpointConfig {

    @Bean
    public CircuitBreakerEndpoint circuitBreakerEndpoint(CircuitBreakerRegistry registry) {
        return new CircuitBreakerEndpoint(registry);
    }

    @Bean
    public RateLimiterEndpoint rateLimiterEndpoint(RateLimiterRegistry registry) {
        return new RateLimiterEndpoint(registry);
    }

    @Bean
    public BulkheadEndpoint bulkheadEndpoint(BulkheadRegistry registry) {
        return new BulkheadEndpoint(registry);
    }


    @Bean
    public RetryEndpoint retryEndpoint(RetryRegistry registry) {
        return new RetryEndpoint(registry);
    }

    @Bean
    public TimeLimiterEndpoint timeLimiterEndpoint(TimeLimiterRegistry registry) {
        return new TimeLimiterEndpoint(registry);
    }
}
