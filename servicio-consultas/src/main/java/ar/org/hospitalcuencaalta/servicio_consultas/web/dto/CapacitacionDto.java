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
public class CapacitacionDto {
    private Long id;
    private String nombreCurso;
    private String institucion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private Long empleadoId;
}
