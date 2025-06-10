package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionProjection {
    @Id
    private Long id;
    private String periodo;
    private Integer puntaje;
    private String comentarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_evaluacion_empleado"))
    private EmpleadoProjection empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluador_id", foreignKey = @ForeignKey(name = "fk_evaluacion_evaluador"))
    private EmpleadoProjection evaluador;
}