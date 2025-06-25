package ar.org.hospitalcuencaalta.servicio_orquestador.excepcion;

/**
 * Excepción lanzada cuando un microservicio requerido no está disponible.
 */
public class ServicioNoDisponibleException extends RuntimeException {
    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
