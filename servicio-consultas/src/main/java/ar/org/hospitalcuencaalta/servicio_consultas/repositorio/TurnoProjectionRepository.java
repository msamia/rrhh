package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.TurnoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoProjectionRepository extends JpaRepository<TurnoProjection,Long> {
    void deleteByJornada_Contrato_Empleado_Id(Long empleadoId);
}
