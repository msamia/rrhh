package ar.org.hospitalcuencaalta.servicio_consultas.repositorio;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.DocumentacionProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentacionProjectionRepository extends JpaRepository<DocumentacionProjection, Long> {
    void deleteByEmpleado_Id(Long empleadoId);
}
