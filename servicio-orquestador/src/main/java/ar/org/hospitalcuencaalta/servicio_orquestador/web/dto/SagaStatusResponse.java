package ar.org.hospitalcuencaalta.servicio_orquestador.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * DTO para devolver el estado final (o intermedio) de una ejecución de la SAGA.
 */
@Data
@Builder
public class SagaStatusResponse {
    /**
     * Identificador generado de la saga.
     */
    private String sagaId;

    /**
     * Estado en que quedó la máquina al finalizar la llamada,
     * p.ej. INICIO, EMPLEADO_CREADO, EMPLEADO_EXISTENTE, CONTRATO_CREADO, FINALIZADA, FALLIDA, etc.
     */
    private String estadoActual;

    /**
     * ID del empleado que se creó (o `null` si no se creó).
     */
    private Long idEmpleadoCreado;

    /**
     * ID del contrato que se creó (o `null` si no se creó).
     */
    private Long idContratoCreado;

    /**
     * Si hay un mensaje de error (p.ej. “Documento ya existe” o “Timeout creando empleado”),
     * este campo lo contendrá. Si `null`, todo fue exitoso.
     */
    private String mensajeError;

    /**
     * Timestamp en que se invocó la SAGA por primera vez (INICIO).
     */
    private Instant timestampInicio;

    /**
     * Timestamp en que la máquina de estados terminó (podría ser FINALIZADA, EMPLEADO_EXISTENTE o FALLIDA).
     */
    private Instant timestampFin;
}
