package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.ContratoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoProjectionRepository extends JpaRepository<ContratoProjection,Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
