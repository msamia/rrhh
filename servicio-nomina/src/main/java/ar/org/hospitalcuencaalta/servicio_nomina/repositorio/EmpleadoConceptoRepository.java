package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.EmpleadoConcepto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoConceptoRepository extends JpaRepository<EmpleadoConcepto, Long> {
    List<EmpleadoConcepto> findByEmpleadoId(Long empleadoId);
}
