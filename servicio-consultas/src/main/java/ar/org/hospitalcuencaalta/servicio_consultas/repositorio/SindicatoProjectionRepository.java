package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.SindicatoProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SindicatoProjectionRepository extends JpaRepository<SindicatoProjection, Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
