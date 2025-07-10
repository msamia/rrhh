package ar.org.hospitalcuencaalta.comunes.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS compartida entre todos los microservicios.
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Que se permita que la puerta de enlace de API gestione los encabezados CORS
                // para evitar la duplicación de respuestas Access-Control-Allow-*,
                // que provocaban el rechazo de solicitudes por parte de los navegadores.
                // Dejar la asignación vacía deshabilita la generación automática de encabezados en
                // los microservicios subyacentes.
            }
        };
    }
}
