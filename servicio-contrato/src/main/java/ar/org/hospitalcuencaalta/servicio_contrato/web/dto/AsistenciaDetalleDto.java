package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaDetalleDto {
    private Long id;
    private LocalDateTime marcacionEntrada;
    private LocalDateTime marcacionSalida;
    private String estado;
    private TurnoDetalleDto turnoDetalleDto;
    private EmpleadoRegistryDto empleado;
}
