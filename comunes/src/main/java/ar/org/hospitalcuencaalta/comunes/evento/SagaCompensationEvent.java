package ar.org.hospitalcuencaalta.comunes.evento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SagaCompensationEvent {
    private Long empleadoId;
    private Long contratoId;
    private Long trainingId;
    private Long payrollId;
}
