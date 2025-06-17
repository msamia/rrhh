package ar.org.hospitalcuencaalta.servicio_orquestador.modelo;

public enum Estados {
    INICIO,               // Estado inicial, antes de arrancar la SAGA
    CREAR_EMPLEADO,       // Acción de intentar crear al empleado
    EMPLEADO_EXISTE,      // Si el documento ya existía, terminamos aquí
    EMPLEADO_CREADO,      // Se creó el empleado correctamente
    CREAR_CONTRATO,       // Acción de intentar crear el contrato
    CONTRATO_CREADO,      // Se creó el contrato correctamente
    ACTUALIZAR_EMPLEADO,  // Actualizar datos de empleado
    EMPLEADO_ACTUALIZADO, // Resultado de actualizar empleado
    ACTUALIZAR_CONTRATO,  // Actualizar datos de contrato
    CONTRATO_ACTUALIZADO, // Resultado de actualizar contrato
    ELIMINAR_CONTRATO,    // Borrar contrato existente
    CONTRATO_ELIMINADO,   // Contrato eliminado correctamente
    ELIMINAR_EMPLEADO,    // Borrar empleado existente
    EMPLEADO_ELIMINADO,   // Empleado eliminado correctamente
    COMPENSAR_EMPLEADO,   // Estamos en rollback: se va a eliminar al empleado
    REVERTIDA,            // Circuit Breaker de empleado se abrió (revertir sin intentar contrato)
    FALLIDA,              // Caso general de error irrecuperable (sin fallback)
    FINALIZADA            // Exito completo (empleado+contrato creados)
}
