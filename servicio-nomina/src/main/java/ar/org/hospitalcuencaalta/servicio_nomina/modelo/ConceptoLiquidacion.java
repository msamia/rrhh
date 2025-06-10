package ar.org.hospitalcuencaalta.servicio_nomina.modelo;

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
public class ConceptoLiquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private String descripcion;
    private java.math.BigDecimal monto;

    @Column(name = "liquidacion_id", insertable = false, updatable = false)
    private Long liquidacionId;    // ahora usa el mismo nombre lógico

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidacion_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_concepto_liquidacion"))
    private Liquidacion liquidacion;
}