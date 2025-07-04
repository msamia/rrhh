package ar.org.hospitalcuencaalta.servicio_entrenamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "ar.org.hospitalcuencaalta")
@EnableFeignClients(basePackages = "ar.org.hospitalcuencaalta.servicio_entrenamiento.feign")
public class ServicioEntrenamientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioEntrenamientoApplication.class, args);
    }

}
