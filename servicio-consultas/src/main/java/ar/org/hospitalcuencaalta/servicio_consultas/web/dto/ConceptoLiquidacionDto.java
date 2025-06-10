package ar.org.hospitalcuencaalta.servicio_consultas.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConceptoLiquidacionDto {
    private Long id;
    private String codigo;
    private String descripcion;
    private java.math.BigDecimal monto;
    private Long liquidacionId;
}
