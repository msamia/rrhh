package ar.org.hospitalcuencaalta.servicio_nomina.repositorio;

import ar.org.hospitalcuencaalta.servicio_nomina.modelo.ConceptoLiquidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import ar.org.hospitalcuencaalta.servicio_nomina.modelo.TipoCalculo;

public interface ConceptoLiquidacionRepository extends JpaRepository<ConceptoLiquidacion, Long> {
    Optional<ConceptoLiquidacion> findByCodigoAndTipoCalculo(String codigo, TipoCalculo tipoCalculo);
}
