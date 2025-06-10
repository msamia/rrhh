package ar.org.hospitalcuencaalta.servicio_empleado.evento;

import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoEventPublisher {
    @Autowired
    private KafkaTemplate<String, EmpleadoDto> kafka;

    public void publishCreated(EmpleadoDto dto) {
        kafka.send("employee.created", dto);
    }

    public void publishUpdated(EmpleadoDto dto) {
        kafka.send("employee.updated", dto);
    }

    public void publishDeleted(EmpleadoDto dto) {
        kafka.send("employee.deleted", dto);
    }
}
