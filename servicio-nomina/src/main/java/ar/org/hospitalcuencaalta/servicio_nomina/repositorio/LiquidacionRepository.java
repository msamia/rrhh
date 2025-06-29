package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {
    Optional<Liquidacion> findByPeriodoAndEmpleadoId(String periodo, Long empleadoId);
}
