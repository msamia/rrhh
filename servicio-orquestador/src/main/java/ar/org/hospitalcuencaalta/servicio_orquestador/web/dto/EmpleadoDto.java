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
public class EmpleadoDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String documento;
    private LocalDate fechaNacimiento;
}
