package ar.org.hospitalcuencaalta.servidor_para_monitoreo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ar.org.hospitalcuencaalta")
@EnableAdminServer
public class ServidorParaMonitoreoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorParaMonitoreoApplication.class, args);
    }
}
