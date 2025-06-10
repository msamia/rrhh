package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conceptos_liquidacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConceptoLiquidacionProjection {
    @Id
    private Long id;
    private String codigo;
    private String descripcion;
    private java.math.BigDecimal monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidacion_id", foreignKey = @ForeignKey(name = "fk_concepto_liquidacion"))
    private LiquidacionProjection liquidacion;
}
