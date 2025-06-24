package ar.org.hospitalcuencaalta.servicio_orquestador.config;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.springboot3.bulkhead.monitoring.endpoint.BulkheadEndpoint;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.springboot3.circuitbreaker.monitoring.endpoint.CircuitBreakerEndpoint;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.springboot3.ratelimiter.monitoring.endpoint.RateLimiterEndpoint;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.springboot3.retry.monitoring.endpoint.RetryEndpoint;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.github.resilience4j.springboot3.timelimiter.monitoring.endpoint.TimeLimiterEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * Registers Resilience4j actuator endpoints when auto-configuration is not
 * activated. These beans expose circuit breakers, rate limiters, bulkheads and
 * other resilience components through Spring Boot Actuator.
 */
@Configuration
public class Resilience4jEndpointConfig {

    @Bean
    @ConditionalOnMissingBean
    public CircuitBreakerEndpoint circuitBreakerEndpoint(CircuitBreakerRegistry registry) {
        return new CircuitBreakerEndpoint(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public RateLimiterEndpoint rateLimiterEndpoint(RateLimiterRegistry registry) {
        return new RateLimiterEndpoint(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public BulkheadEndpoint bulkheadEndpoint(BulkheadRegistry bulkheadRegistry,
                                             ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry) {
        return new BulkheadEndpoint(bulkheadRegistry, threadPoolBulkheadRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    public RetryEndpoint retryEndpoint(RetryRegistry registry) {
        return new RetryEndpoint(registry);
    }

    @Bean
    @ConditionalOnMissingBean
    public TimeLimiterEndpoint timeLimiterEndpoint(TimeLimiterRegistry registry) {
        return new TimeLimiterEndpoint(registry);
    }
}