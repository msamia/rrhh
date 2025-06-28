package ar.org.hospitalcuencaalta.servicio_nomina.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ar.org.hospitalcuencaalta.servicio_nomina.modelo.TipoCalculo;


@Entity
@Table(name = "conceptos_liquidacion",
        uniqueConstraints = @UniqueConstraint(name = "uk_codigo_tipocalculo",
                columnNames = {"codigo", "tipo_calculo"}))
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_calculo")
    private TipoCalculo tipoCalculo;

}