package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "contratos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoProjection {
    @Id
    private Long id;
    private String tipoContrato;
    private String regimen;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_contrato_empleado"))
    private EmpleadoProjection empleado;
}
