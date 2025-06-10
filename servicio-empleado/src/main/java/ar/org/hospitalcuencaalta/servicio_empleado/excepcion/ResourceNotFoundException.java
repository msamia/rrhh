package ar.org.hospitalcuencaalta.servicio_empleado.excepcion;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entidad, Object id) {
        super(String.format("%s con id '%s' no encontrado", entidad, id));
    }
}


