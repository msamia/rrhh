package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "capacitaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacitacionProjection {
    @Id
    private Long id;
    private String nombreCurso;
    private String institucion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_capacitacion_empleado"))
    private EmpleadoProjection empleado;
}
