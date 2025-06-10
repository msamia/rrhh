package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "vacaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer ejercicio;
    private Integer diasOtorgados;
    private Integer diasTomados;

    @Formula("empleado_id")
    private Long empleadoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_vacacion_employee"))
    private EmpleadoRegistry empleado;
}