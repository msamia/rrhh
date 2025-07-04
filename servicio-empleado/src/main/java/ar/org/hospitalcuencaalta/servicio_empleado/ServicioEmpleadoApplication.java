package ar.org.hospitalcuencaalta.servicio_empleado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ar.org.hospitalcuencaalta")
public class ServicioEmpleadoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioEmpleadoApplication.class, args);
    }

}
