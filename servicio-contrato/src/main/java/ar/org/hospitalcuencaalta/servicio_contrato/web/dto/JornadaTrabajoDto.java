package ar.org.hospitalcuencaalta.servicio_contrato.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JornadaTrabajoDto {
    private Long id;
    private String tipoJornada;
    private Integer horasSemanales;
    private Long contratoId;    // ← sólo el ID
}
