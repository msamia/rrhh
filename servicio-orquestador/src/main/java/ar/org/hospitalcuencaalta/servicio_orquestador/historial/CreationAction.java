package ar.org.hospitalcuencaalta.servicio_orquestador.historial;

import java.time.Instant;

public record CreationAction(String step, boolean success, String error, Instant timestamp) {
}
