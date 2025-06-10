package ar.org.hospitalcuencaalta.servicio_nomina.modelo;

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
public class Liquidacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String periodo;
    private java.math.BigDecimal sueldoBruto;
    private java.math.BigDecimal descuentos;
    private java.math.BigDecimal sueldoNeto;

    @Column(name = "empleado_id", insertable = false, updatable = false)
    private Long empleadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_liquidacion_empleado"))
    private EmpleadoRegistry empleado;

}