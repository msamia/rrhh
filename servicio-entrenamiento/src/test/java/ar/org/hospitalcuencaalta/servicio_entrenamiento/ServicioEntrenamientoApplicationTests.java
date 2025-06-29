package ar.org.hospitalcuencaalta.servicio_entrenamiento;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "spring.kafka.consumer.group-id=test",
        "spring.kafka.listener.auto-startup=false"
})
class ServicioEntrenamientoApplicationTests {

    @Test
    void contextLoads() {
    }

}
