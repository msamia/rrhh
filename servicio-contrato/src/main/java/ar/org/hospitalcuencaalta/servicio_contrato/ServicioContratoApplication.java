package ar.org.hospitalcuencaalta.servicio_contrato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "ar.org.hospitalcuencaalta.servicio_contrato.feign")
public class ServicioContratoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioContratoApplication.class, args);
    }

}
