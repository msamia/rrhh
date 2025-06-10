package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacitacionDetalleDto {
    private Long id;
    private String nombreCurso;
    private String institucion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private EmpleadoRegistryDto empleado;
}
