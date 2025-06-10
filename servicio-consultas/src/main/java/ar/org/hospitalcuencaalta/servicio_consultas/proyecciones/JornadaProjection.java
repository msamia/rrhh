package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jornadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornadaProjection {
    @Id
    private Long id;
    private String tipoJornada;
    private Integer horasSemanales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", foreignKey = @ForeignKey(name = "fk_jornada_contrato"))
    private ContratoProjection contrato;
}