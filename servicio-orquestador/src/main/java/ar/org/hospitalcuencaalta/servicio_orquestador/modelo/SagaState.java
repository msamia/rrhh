package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

import jakarta.persistence.*;
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
    @javax.persistence.GeneratedValue(generator = "UUID")
    @GeneratedValue(generator = "uuid")
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
