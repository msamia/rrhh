package ar.org.hospitalcuencaalta.servicio_orquestador.controlador;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationHistory;
import ar.org.hospitalcuencaalta.servicio_orquestador.historial.CreationAction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador REST explícito para exponer el estado del disyuntor cuando la configuración automática del actuador
 * no registra los puntos finales predeterminados.
 */
@RestController
@RequestMapping("/actuator/cb-state")
public class CircuitBreakerStatusController {

    private final CircuitBreakerRegistry registry;
    private final CreationHistory history;

    public CircuitBreakerStatusController(CircuitBreakerRegistry registry,
                                          CreationHistory history) {
        this.registry = registry;
        this.history = history;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getState(@PathVariable String name,
                                                        @RequestParam(defaultValue = "false") boolean includeState) {
        CircuitBreaker cb = registry.getAllCircuitBreakers()
                .stream()
                .filter(b -> b.getName().equals(name))
                .findFirst()
                .orElse(null);
        if (cb == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", cb.getName());
        if (includeState) {
            data.put("state", cb.getState().toString());
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/empleado-actions")
    public ResponseEntity<java.util.List<CreationAction>> allActions() {
        return ResponseEntity.ok(history.creationAttempts());
    }
}
