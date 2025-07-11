package ar.org.hospitalcuencaalta.servicio_consultas.controlador;

import ar.org.hospitalcuencaalta.servicio_consultas.proyecciones.AsistenciaProjection;
import ar.org.hospitalcuencaalta.servicio_consultas.repositorio.AsistenciaProjectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@Tag(name = "Consulta Asistencias", description = "Consultas de asistencias registradas")
public class AsistenciaQueryController {
    @Autowired
    private AsistenciaProjectionRepository repo;

    @GetMapping
    @Operation(summary = "Listar asistencias", description = "Obtiene todas las asistencias registradas")
    public List<AsistenciaProjection> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Asistencia por id", description = "Devuelve una asistencia específica")
    public AsistenciaProjection get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}

