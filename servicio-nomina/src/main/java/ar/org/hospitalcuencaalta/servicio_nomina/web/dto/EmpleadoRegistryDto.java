package ar.org.hospitalcuencaalta.servicio_nomina.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoRegistryDto {
    private Long id;
    private String documento;
    private String nombre;
    private String apellido;
}

