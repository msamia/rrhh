package ar.org.hospitalcuencaalta.servicio_orquestador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.statemachine.config.EnableStateMachine;

@SpringBootApplication
@EnableFeignClients
@EnableStateMachine
@EnableDiscoveryClient
public class ServicioOrquestadorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioOrquestadorApplication.class, args);
    }

}
