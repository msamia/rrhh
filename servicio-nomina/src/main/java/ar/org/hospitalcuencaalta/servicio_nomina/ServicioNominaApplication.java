package ar.org.hospitalcuencaalta.servicio_nomina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "ar.org.hospitalcuencaalta.servicio_nomina.feign")
public class ServicioNominaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioNominaApplication.class, args);
    }

}
