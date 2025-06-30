package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sindicatos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SindicatoProjection {
    @Id
    private Long id;
    private String nombre;
    @Column(name = "convenio_colectivo")
    private String convenioColectivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_sindicato_empleado"))
    private EmpleadoProjection empleado;
}
