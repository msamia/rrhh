package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;


import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Page<Departamento> findByEmpleadoId(Long empleadoId, Pageable pageable);

    Optional<Departamento> findByIdAndEmpleadoId(Long id, Long empleadoId);
}
