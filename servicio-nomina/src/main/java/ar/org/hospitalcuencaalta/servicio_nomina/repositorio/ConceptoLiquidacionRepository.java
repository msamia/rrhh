package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConceptoLiquidacionRepository extends JpaRepository<ConceptoLiquidacion, Long> {
    List<ConceptoLiquidacion> findByLiquidacionId(Long liquidacionId);
    List<ConceptoLiquidacion> findByEmpleadoId(Long empleadoId);
}
