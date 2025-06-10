package ar.org.hospitalcuencaalta.servicio_nomina.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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