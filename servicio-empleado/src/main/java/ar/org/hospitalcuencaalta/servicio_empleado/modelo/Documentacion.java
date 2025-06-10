package ar.org.hospitalcuencaalta.servicio_empleado.modelo;

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
public class Documentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "ruta_archivo")
    private String rutaArchivo;
}