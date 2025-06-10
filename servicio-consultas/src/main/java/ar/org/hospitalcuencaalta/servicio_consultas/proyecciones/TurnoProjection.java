package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoProjection {
    @Id
    private Long id;
    private LocalDateTime fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String tipoTurno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jornada_id", foreignKey = @ForeignKey(name = "fk_turno_jornada"))
    private JornadaProjection jornada;
}