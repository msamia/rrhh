package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "puestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuestoProjection {
    @Id
    private Long id;
    private String titulo;
    @Column(name = "nivel_jerarquico")
    private String nivelJerarquico;
    @Column(name = "descripcion_funciones")
    private String descripcionFunciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_puesto_empleado"))
    private EmpleadoProjection empleado;
}
