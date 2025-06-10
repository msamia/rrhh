package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

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
