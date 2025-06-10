package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Table(name = "turnos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Turno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String tipoTurno;

    @Formula("jornada_id")
    private Long jornadaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jornada_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_turno_jornada"))
    private JornadaTrabajo jornada;
}
