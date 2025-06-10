package ar.org.hospitalcuencaalta.servicio_entrenamiento.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionDetalleDto {
    private Long id;
    private String periodo;
    private Integer puntaje;
    private String comentarios;
    private EmpleadoRegistryDto empleado;
    private EmpleadoRegistryDto evaluador;
}
