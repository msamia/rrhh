package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asistencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime marcacionEntrada;
    private LocalDateTime marcacionSalida;
    private String estado;

    @Formula("turno_id")
    private Long turnoId;
    @Formula("empleado_id")
    private Long empleadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_asistencia_turno"))
    private Turno turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_asistencia_employee"))
    private EmpleadoRegistry empleado;
}
