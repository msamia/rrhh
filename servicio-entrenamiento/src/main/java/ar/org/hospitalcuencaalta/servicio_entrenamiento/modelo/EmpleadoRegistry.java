package ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Entity
@Table(name="empleado_registry")
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

