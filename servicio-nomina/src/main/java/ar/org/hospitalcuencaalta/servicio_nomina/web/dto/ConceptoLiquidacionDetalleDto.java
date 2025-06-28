package ar.org.hospitalcuencaalta.servicio_nomina.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ar.org.hospitalcuencaalta.servicio_nomina.modelo.TipoCalculo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConceptoLiquidacionDetalleDto {
    private Long id;
    private String codigo;
    private String descripcion;
    private java.math.BigDecimal monto;
    private TipoCalculo tipoCalculo;
}
