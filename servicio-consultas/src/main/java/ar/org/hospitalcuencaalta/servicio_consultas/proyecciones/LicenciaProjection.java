package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "licencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenciaProjection {
    @Id
    private Long id;
    private String tipo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_licencia_empleado"))
    private EmpleadoProjection empleado;
}
