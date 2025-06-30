package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

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
public class EmpleadoConceptoProjection {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concepto_id", foreignKey = @ForeignKey(name = "fk_empconcepto_concepto"))
    private ConceptoLiquidacionProjection concepto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_empconcepto_empleado"))
    private EmpleadoProjection empleado;
}
