package ar.org.hospitalcuencaalta.servicio_contrato.repositorio;

import ar.org.hospitalcuencaalta.servicio_contrato.modelo.Vacacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacacionRepository extends JpaRepository<Vacacion, Long> {}