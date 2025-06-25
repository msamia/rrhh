package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Documentacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentacionRepository extends JpaRepository<Documentacion, Long> {

    Page<Documentacion> findByEmpleadoId(Long empleadoId, Pageable pageable);

    Optional<Documentacion> findByIdAndEmpleadoId(Long id, Long empleadoId);
}
