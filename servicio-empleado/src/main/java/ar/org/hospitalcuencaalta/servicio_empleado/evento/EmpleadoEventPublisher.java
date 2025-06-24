package ar.org.hospitalcuencaalta.servicio_empleado.evento;

import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoEventPublisher {
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    public void publishCreated(EmpleadoDto dto) {
        kafka.send("empleado.created", dto);
    }

    public void publishUpdated(EmpleadoDto dto) {
        kafka.send("empleado.updated", dto);
    }

    public void publishDeleted(Long id) {
        kafka.send("empleado.deleted", id);
    }
}
