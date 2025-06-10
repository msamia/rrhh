package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vacaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacacionProjection {
    @Id
    private Long id;
    private Integer ejercicio;
    private Integer diasOtorgados;
    private Integer diasTomados;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_vacacion_empleado"))
    private EmpleadoProjection empleado;
}
