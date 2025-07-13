package ar.org.hospitalcuencaalta.comunes.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador simple para responder en la raíz de cada microservicio.
 * Evita que se muestre la página Whitelabel Error Page cuando se accede
 * directamente al servicio desde Spring Boot Admin.
 */
@RestController
public class RootController {

    @Value("${spring.application.name:application}")
    private String appName;

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "<!DOCTYPE html><html><body><h1>" + appName +
               "</h1><p>Servicio en funcionamiento.</p></body></html>";
    }
}
