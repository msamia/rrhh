package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.PuestoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuestoProjectionRepository extends JpaRepository<PuestoProjection, Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
