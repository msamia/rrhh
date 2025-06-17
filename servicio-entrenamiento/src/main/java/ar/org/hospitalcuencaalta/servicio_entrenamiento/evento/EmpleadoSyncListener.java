package ar.org.hospitalcuencaalta.servicio_entrenamiento.evento;

import ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto.EmpleadoRegistryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoSyncListener {
    @Autowired
    private JdbcTemplate jdbc;

    @KafkaListener(topics="empleado.created")
    public void onCreated(EmpleadoRegistryDto dto) {
        jdbc.update("INSERT INTO employee_registry(id,legajo,nombre,apellido) VALUES (?,?,?,?)",
                dto.getId(), dto.getLegajo(), dto.getNombre(), dto.getApellido());
    }
    @KafkaListener(topics="empleado.updated")
    public void onUpdated(EmpleadoRegistryDto dto) {
        jdbc.update("UPDATE employee_registry SET legajo=?,nombre=?,apellido=? WHERE id=?",
                dto.getLegajo(), dto.getNombre(), dto.getApellido(), dto.getId());
    }
    @KafkaListener(topics="empleado.deleted")
    public void onDeleted(Long id) {
        jdbc.update("DELETE FROM employee_registry WHERE id=?", id);
    }
}
