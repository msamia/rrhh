package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

/**
 * Todos los eventos necesarios para la SAGA, tal como tú los definiste.
 */
public enum Eventos {
    // 1) Iniciar creación del empleado
    SOLICITAR_CREAR_EMPLEADO,

    // 2) Empleado ya existe (documento duplicado)
    EMPLEADO_EXISTE,

    // 3) Empleado creado exitosamente
    EMPLEADO_CREADO,

    // 4) Fallback de cliente de empleado (CircuitBreaker atrapado)
    FALLBACK_EMPLEADO,

    // 5) Iniciar creación del contrato (se genera tras EMPLEADO_CREADO)
    SOLICITAR_CREAR_CONTRATO,

    // 6) Contrato creado exitosamente
    CONTRATO_CREADO,

    // 7) Contrato fallido (por duplicado o error genérico)
    CONTRATO_FALLIDO,

    // 8) Fallback de cliente de contrato (CircuitBreaker atrapado)
    FALLBACK_CONTRATO,

    // 9) Compensar empleado (trigger para eliminar empleado)
    COMPENSAR_EMPLEADO,

    // 10) CircuitBreaker de empleado se abrió → REVERTIR antes de intentar contrato
    CB_REVERTIDO,

    // 11) Señalar final exitoso (empleado+contrato creados)
    FINALIZAR,

    // 12) Evento de error general forzando camino a FALLIDA
    ERROR
}
