package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoProjection {
    @Id
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
