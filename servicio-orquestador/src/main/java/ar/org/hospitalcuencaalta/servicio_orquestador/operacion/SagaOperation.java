package ar.org.hospitalcuencaalta.servicio_orquestador.operacion;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "saga_operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_id")
    private Long sagaId;

    private String step;

    private boolean success;

    @Lob
    private String error;

    private Instant timestamp;
}
