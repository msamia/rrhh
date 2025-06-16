package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saga_states")
public class SagaState {

    @Id
    @Column(name = "saga_id")
    private String sagaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estados estado;

    @Lob
    @Column(name = "extended_state", columnDefinition = "LONGTEXT")
    private String extendedState;

    @Column(name = "updated_at")
    private Instant updatedAt;
}
