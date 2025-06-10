package ar.org.hospitalcuencaalta.servicio_contrato.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad ContratoLaboral. NO hay @ManyToOne contra EmpleadoRegistry,
 * ni @ForeignKey, porque “el empleado” vive en otra base.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contratos")
public class ContratoLaboral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Este campo “simula” la FK: al crearse el contrato,
     * el servicio verificará que ese empleadoId exista en servicio-empleado.
     */
    @Column(name = "empleado_id", nullable = false)
    private Long empleadoId;

    @Column(name = "fecha_desde", nullable = false)
    private LocalDate fechaDesde;

    @Column(name = "fecha_hasta", nullable = false)
    private LocalDate fechaHasta;

    @Column(name = "tipo_contrato", nullable = false)
    private String tipoContrato;

    @Column(name = "regimen", nullable = false)
    private String regimen;

    @Column(name = "salario", nullable = false)
    private Double salario;
}

