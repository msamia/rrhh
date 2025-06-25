package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Sindicato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SindicatoRepository extends JpaRepository<Sindicato, Long> {

    Page<Sindicato> findByEmpleadoId(Long empleadoId, Pageable pageable);

    Optional<Sindicato> findByIdAndEmpleadoId(Long id, Long empleadoId);
}
