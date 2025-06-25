package ar.org.hospitalcuencaalta.servicio_empleado.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentacionDetalleDto {
    private Long id;
    private String tipoDocumento;
    private String rutaArchivo;
    private EmpleadoDto empleado;
}
