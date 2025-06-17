package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;


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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saga_id", columnDefinition = "BIGINT", updatable = false, nullable = false)
    private Long sagaId;




    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private Estados estado;

    @Lob
    @Column(name = "extended_state", columnDefinition = "LONGTEXT")
    private String extendedState;

    @Column(name = "updated_at")
    private Instant updatedAt;

}
