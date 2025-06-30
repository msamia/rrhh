package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.EmpleadoConceptoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoConceptoProjectionRepository extends JpaRepository<EmpleadoConceptoProjection, Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
