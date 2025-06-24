package ar.org.hospitalcuencaalta.servicio_orquestador.historial;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CreationHistory {
    private final List<CreationAction> actions = new CopyOnWriteArrayList<>();

    public void add(CreationAction action) {
        actions.add(action);
    }

    public List<CreationAction> all() {
        return List.copyOf(actions);
    }

    /**
     * Returns only actions related to the creation of an empleado or its contrato.
     */
    public List<CreationAction> creationAttempts() {
        return actions.stream()
                .filter(a -> a.step().startsWith("CREATE_EMPLEADO") ||
                             a.step().startsWith("CREATE_CONTRATO"))
                .toList();
    }
}
