package ar.org.hospitalcuencaalta.servicio_consultas.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoLaboralDto {
    private Long id;
    private String tipoContrato;
    private String regimen;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Long empleadoId;
}
