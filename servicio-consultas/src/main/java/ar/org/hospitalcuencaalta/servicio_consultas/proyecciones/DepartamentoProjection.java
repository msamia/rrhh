package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartamentoProjection {
    @Id
    private Long id;
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_departamento_empleado"))
    private EmpleadoProjection empleado;
}
