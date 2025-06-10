package ar.org.hospitalcuencaalta.servicio_consultas.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiquidacionDto {
    private Long id;
    private String periodo;
    private java.math.BigDecimal sueldoBruto;
    private java.math.BigDecimal descuentos;
    private java.math.BigDecimal sueldoNeto;
    private Long empleadoId;
}