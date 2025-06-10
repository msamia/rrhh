package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {}
