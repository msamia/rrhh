package ar.org.hospitalcuencaalta.api_gateway.config;

import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides a default {@link WebFluxProperties} bean so that Spring Cloud Gateway
 * auto-configuration can create its predicates during tests.
 */
@Configuration
public class WebFluxPropertiesConfig {

    @Bean
    public WebFluxProperties webFluxProperties() {
        return new WebFluxProperties();
    }
}
