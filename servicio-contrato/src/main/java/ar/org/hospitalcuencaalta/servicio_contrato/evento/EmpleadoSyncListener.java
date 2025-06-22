package ar.org.hospitalcuencaalta.servicio_contrato.evento;

import ar.org.hospitalcuencaalta.servicio_contrato.web.dto.EmpleadoRegistryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmpleadoSyncListener {
    @Autowired
    private JdbcTemplate jdbc;

    @KafkaListener(topics = "empleado.created")
    public void onCreated(EmpleadoRegistryDto dto) {
        try {
            jdbc.update(
                    "INSERT INTO empleado_registry(id, legajo, nombre, apellido) VALUES (?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE legajo=VALUES(legajo), nombre=VALUES(nombre), apellido=VALUES(apellido)",
                    dto.getId(), dto.getLegajo(), dto.getNombre(), dto.getApellido());
        } catch (DuplicateKeyException e) {
            log.error("[EmpleadoSync] Clave duplicada al insertar empleado {}", dto.getId(), e);
        } catch (DataAccessException e) {
            log.error("[EmpleadoSync] Error al insertar empleado {}", dto.getId(), e);
        }
    }

    @KafkaListener(topics = "empleado.updated")
    public void onUpdated(EmpleadoRegistryDto dto) {
        try {
            jdbc.update("UPDATE empleado_registry SET legajo=?, nombre=?, apellido=? WHERE id=?",
                    dto.getLegajo(), dto.getNombre(), dto.getApellido(), dto.getId());
        } catch (DataAccessException e) {
            log.error("[EmpleadoSync] Error al actualizar empleado {}", dto.getId(), e);
        }
    }

    @KafkaListener(topics = "empleado.deleted")
    public void onDeleted(Long id) {
        try {
            jdbc.update("DELETE FROM empleado_registry WHERE id=?", id);
        } catch (DataAccessException e) {
            log.error("[EmpleadoSync] Error al eliminar empleado {}", id, e);
        }
    }
}
