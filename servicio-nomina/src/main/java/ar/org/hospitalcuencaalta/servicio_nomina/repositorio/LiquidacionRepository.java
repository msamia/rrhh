package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.Liquidacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiquidacionRepository extends JpaRepository<Liquidacion, Long> {}
