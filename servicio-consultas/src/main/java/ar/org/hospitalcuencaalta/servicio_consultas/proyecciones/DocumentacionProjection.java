package ar.org.hospitalcuencaalta.servicio_consultas.proyecciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documentaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentacionProjection {
    @Id
    private Long id;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", foreignKey = @ForeignKey(name = "fk_documentacion_empleado"))
    private EmpleadoProjection empleado;
}
