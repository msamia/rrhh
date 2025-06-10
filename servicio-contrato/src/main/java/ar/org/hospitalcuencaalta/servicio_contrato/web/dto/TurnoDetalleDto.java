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
public class TurnoDetalleDto {
    private Long id;
    private LocalDateTime fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String tipoTurno;
    private JornadaTrabajoDetalleDto jornadaTrabajoDetalleDto;
}
