package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoLaboralDetalleDto {
    private Long id;
    private String tipoContrato;
    private String regimen;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private Double salario;
    private EmpleadoRegistryDto empleado;
}
