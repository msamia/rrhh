package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornadaTrabajoDetalleDto {
    private Long id;
    private String tipoJornada;
    private Integer horasSemanales;
    private Long contratoId;
    // DTO de detalle para incluir datos de Empleado dentro del Contrato
    private ContratoLaboralDetalleDto contrato;
}