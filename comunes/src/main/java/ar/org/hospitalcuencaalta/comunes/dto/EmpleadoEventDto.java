package ar.org.hospitalcuencaalta.comunes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoEventDto {
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
}
