package ar.org.hospitalcuencaalta.servicio_empleado.excepcion;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}