package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saga_states")
public class SagaState {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(java.sql.Types.VARCHAR)
    @Column(name = "saga_id", length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private java.util.UUID sagaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estados estado;

    @Lob
    @Column(name = "extended_state", columnDefinition = "LONGTEXT")
    private String extendedState;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
