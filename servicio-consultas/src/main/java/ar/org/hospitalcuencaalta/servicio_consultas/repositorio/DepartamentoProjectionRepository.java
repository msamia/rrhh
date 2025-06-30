package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.DepartamentoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoProjectionRepository extends JpaRepository<DepartamentoProjection, Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
