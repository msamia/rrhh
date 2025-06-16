package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    @JsonAlias("tipo")
    private String tipoContrato;
    private String regimen;
    @JsonAlias("fechaInicio")
    private LocalDate fechaDesde;
    @JsonAlias("fechaFin")
    private LocalDate fechaHasta;
    private Double salario;
    private Long empleadoId;
}
