package ar.org.hospitalcuencaalta.servicio_empleado.evento;

import ar.org.hospitalcuencaalta.servicio_empleado.web.dto.EmpleadoDto;
import ar.org.hospitalcuencaalta.comunes.dto.EmpleadoEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoEventPublisher {
    @Autowired
    private KafkaTemplate<String, Object> kafka;

    private EmpleadoEventDto map(EmpleadoDto dto) {
        return EmpleadoEventDto.builder()
                .id(dto.getId())
                .documento(dto.getDocumento())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .build();
    }

    public void publishCreated(EmpleadoDto dto) {
        kafka.send("empleado.created", map(dto));
    }

    public void publishUpdated(EmpleadoDto dto) {
        kafka.send("empleado.updated", map(dto));
    }

    public void publishDeleted(Long id) {
        kafka.send("empleado.deleted", id);
    }
}
