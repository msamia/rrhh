package ar.org.hospitalcuencaalta.servicio_orquestador.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompensacionDto {
    private Long id;
    private Long empleadoId;
    private LocalDateTime fechaCompensacion;
    private String motivo;
}