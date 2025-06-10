package ar.org.hospitalcuencaalta.servicio_empleado.web.dto;

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
    private String legajo;
    private String apellido;
    private String nombre;
    private String documento;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String domicilio;
    private String telefono;
    private String email;
    private LocalDate fechaIngreso;
    private LocalDate fechaEgreso;
}