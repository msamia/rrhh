package ar.org.hospitalcuencaalta.servidor_para_descubrimiento;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServidorParaDescubrimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorParaDescubrimientoApplication.class, args);
    }

}
