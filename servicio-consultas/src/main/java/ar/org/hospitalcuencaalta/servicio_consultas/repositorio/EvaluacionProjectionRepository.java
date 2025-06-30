package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EvaluacionProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluacionProjectionRepository extends JpaRepository<EvaluacionProjection,Long> {
    void deleteByEmpleado_Id(Long empleadoId);
    void deleteByEvaluador_Id(Long evaluadorId);
}
