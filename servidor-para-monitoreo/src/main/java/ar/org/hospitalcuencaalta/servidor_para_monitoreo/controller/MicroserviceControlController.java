package ar.org.hospitalcuencaalta.servidor_para_monitoreo.controller;

import ar.org.hospitalcuencaalta.servidor_para_monitoreo.service.MicroserviceManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/control")
@RequiredArgsConstructor
@Tag(name = "Control de servicios", description = "Iniciar y detener microservicios")
public class MicroserviceControlController {

    private final MicroserviceManager manager;

    @PostMapping("/start")
    @Operation(summary = "Iniciar servicio", description = "Inicia N instancias del microservicio")
    public ResponseEntity<Map<String, Integer>> start(@RequestBody ControlRequest req) throws IOException {
        manager.start(req.service(), req.count());
        return ResponseEntity.ok(manager.status());
    }

    @PostMapping("/stop")
    @Operation(summary = "Detener servicio", description = "Detiene N instancias del microservicio")
    public ResponseEntity<Map<String, Integer>> stop(@RequestBody ControlRequest req) {
        manager.stop(req.service(), req.count());
        return ResponseEntity.ok(manager.status());
    }

    @GetMapping("/status")
    @Operation(summary = "Estado de servicios", description = "Cantidad de instancias en ejecución por servicio")
    public Map<String, Integer> status() {
        return manager.status();
    }

    public record ControlRequest(String service, int count) {}
}
