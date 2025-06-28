package ar.org.hospitalcuencaalta.servicio_nomina.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleado_concepto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoConcepto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empleado_id")
    private Long empleadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concepto_id", foreignKey = @ForeignKey(name = "fk_empconcepto_concepto"))
    private ConceptoLiquidacion concepto;
}
