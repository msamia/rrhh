package ar.org.hospitalcuencaalta.servicio_entrenamiento.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionDesempeno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private YearMonth periodo;
    private Integer puntaje;
    private String comentarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="empleado_id", insertable=false, updatable=false,
            foreignKey=@ForeignKey(name="fk_evaluacion_employee"))
    private EmpleadoRegistry empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluador_id", insertable=false, updatable=false,
            foreignKey=@ForeignKey(name="fk_evaluacion_evaluador"))
    private EmpleadoRegistry evaluador;
}