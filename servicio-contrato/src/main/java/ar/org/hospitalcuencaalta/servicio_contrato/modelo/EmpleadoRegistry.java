package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleado_registry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoRegistry {
    @Id
    private Long id;
    private String legajo;
    private String nombre;
    private String apellido;
}
