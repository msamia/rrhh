package ar.org.hospitalcuencaalta.servicio_empleado.repositorio;

import ar.org.hospitalcuencaalta.servicio_empleado.modelo.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {}