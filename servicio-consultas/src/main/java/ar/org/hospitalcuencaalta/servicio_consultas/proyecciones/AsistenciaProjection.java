package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asistencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaProjection {
    @Id
    private Long id;
    private LocalDateTime marcacionEntrada;
    private LocalDateTime marcacionSalida;
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turno_id", foreignKey = @ForeignKey(name = "fk_asistencia_turno"))
    private TurnoProjection turno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_asistencia_empleado"))
    private EmpleadoProjection empleado;
}
