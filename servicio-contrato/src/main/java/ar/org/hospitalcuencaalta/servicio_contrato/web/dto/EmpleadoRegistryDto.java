package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.*;

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
