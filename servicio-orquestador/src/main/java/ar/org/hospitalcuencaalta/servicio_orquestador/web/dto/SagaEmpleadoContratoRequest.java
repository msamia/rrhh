package ar.org.hospitalcuencaalta.servicio_orquestador.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SagaEmpleadoContratoRequest {
    private EmpleadoDto empleado;
    private ContratoLaboralDto contrato;
}