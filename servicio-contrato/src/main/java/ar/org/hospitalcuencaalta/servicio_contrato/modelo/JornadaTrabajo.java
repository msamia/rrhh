package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "jornadas")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JornadaTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipoJornada;
    private Integer horasSemanales;

    @Formula("contrato_id")
    private Long contratoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_jornada_contrato"))
    private ContratoLaboral contratoLaboral;
}
