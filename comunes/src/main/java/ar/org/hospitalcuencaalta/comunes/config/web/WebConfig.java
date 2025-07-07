package ar.org.hospitalcuencaalta.comunes.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration shared across all microservices.
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Let the API gateway handle CORS headers to avoid duplicating
                // Access-Control-Allow-* responses which caused browsers to
                // reject requests. Leaving the mapping empty disables automatic
                // header generation in the underlying microservices.
            }
        };
    }
}
