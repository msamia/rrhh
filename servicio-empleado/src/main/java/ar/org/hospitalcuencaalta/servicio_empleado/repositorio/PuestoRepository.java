package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Puesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    Page<Puesto> findByEmpleadoId(Long empleadoId, Pageable pageable);

    Optional<Puesto> findByIdAndEmpleadoId(Long id, Long empleadoId);
}
