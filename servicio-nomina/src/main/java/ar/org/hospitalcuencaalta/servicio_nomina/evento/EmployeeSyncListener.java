package ar.org.hospitalcuencaalta.servicio_nomina.evento;

import ar.org.hospitalcuencaalta.comunes.dto.EmpleadoEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class EmployeeSyncListener {
    @Autowired
    private JdbcTemplate jdbc;

    @KafkaListener(topics = "empleado.created")
    public void onCreated(EmpleadoEventDto dto) {
        jdbc.update("INSERT INTO empleado_registry(id,legajo,nombre,apellido) VALUES(?,?,?,?)",
                dto.getId(), dto.getLegajo(), dto.getNombre(), dto.getApellido());
    }

    @KafkaListener(topics = "empleado.updated")
    public void onUpdated(EmpleadoEventDto dto) {
        jdbc.update("UPDATE empleado_registry SET legajo=?,nombre=?,apellido=? WHERE id=?",
                dto.getLegajo(), dto.getNombre(), dto.getApellido(), dto.getId());
    }

    @KafkaListener(topics = "empleado.deleted")
    public void onDeleted(Long id) {
        jdbc.update("DELETE FROM empleado_registry WHERE id=?", id);
    }
}
