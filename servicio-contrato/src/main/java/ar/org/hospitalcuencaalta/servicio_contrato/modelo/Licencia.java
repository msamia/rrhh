package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.time.LocalDate;

@Entity
@Table(name = "licencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Licencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    @Formula("empleado_id")
    private Long empleadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_licencia_employee"))
    private EmpleadoRegistry empleado;
}
