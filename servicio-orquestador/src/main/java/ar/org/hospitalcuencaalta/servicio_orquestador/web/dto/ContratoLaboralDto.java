package ar.org.hospitalcuencaalta.servicio_orquestador.web.dto;

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
    private Long empleadoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String tipo;
    private Double salario;
}