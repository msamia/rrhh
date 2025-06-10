package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "liquidaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiquidacionProjection {
    @Id
    private Long id;
    private String periodo;
    private java.math.BigDecimal sueldoBruto;
    private java.math.BigDecimal descuentos;
    private java.math.BigDecimal sueldoNeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_liquidacion_empleado"))
    private EmpleadoProjection empleado;
}